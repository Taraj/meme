package tk.tarajki.meme.services

import net.bytebuddy.utility.RandomString
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import tk.tarajki.meme.exceptions.UserAuthException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.transaction.annotation.Transactional
import tk.tarajki.meme.dto.JwtAuthResponse
import tk.tarajki.meme.dto.models.*
import tk.tarajki.meme.dto.requests.*
import tk.tarajki.meme.exceptions.ResourceAlreadyExistException
import tk.tarajki.meme.exceptions.ResourceNotFoundException
import tk.tarajki.meme.models.*
import tk.tarajki.meme.repositories.*
import tk.tarajki.meme.security.JwtTokenProvider
import java.time.LocalDateTime
import java.util.concurrent.ThreadLocalRandom


@Service
class UserService(
        private val userRepository: UserRepository,
        private val roleRepository: RoleRepository,
        private val bCryptPasswordEncoder: BCryptPasswordEncoder,
        private val authenticationManager: AuthenticationManager,
        private val jwtTokenProvider: JwtTokenProvider,
        private val banRepository: BanRepository,
        private val warnRepository: WarnRepository,
        private val emailService: EmailService,
        private val passwordResetTokenRepository: PasswordResetTokenRepository,
        private val userFeedbackRepository: UserFeedbackRepository
) {

    fun getAllUserDto(offset: Int, count: Int, dtoFactory: (User) -> UserDto): List<UserDto> {
        val users = userRepository.findAll()
        return users.asSequence()
                .drop(offset)
                .take(count)
                .map(dtoFactory)
                .toList()
    }

    fun getAllUserFeedbackDtoByNickname(nickname: String, offset: Int, count: Int, dtoFactory: (UserFeedback) -> UserFeedbackDto): List<UserFeedbackDto> {
        val userFeedback = userFeedbackRepository.findAll()
        return userFeedback.asSequence()
                .drop(offset)
                .take(count)
                .map(dtoFactory)
                .toList()
    }

    fun getUserDtoByNickname(nickname: String, dtoFactory: (User) -> UserDto): UserDto {
        val user = findUserByNickname(nickname)
        return dtoFactory(user)
    }

    fun getUserBansDtoByNickname(nickname: String, offset: Int, count: Int, dtoFactory: (Ban) -> BanDto): List<BanDto> {
        val bans = findUserByNickname(nickname).bans ?: return emptyList()
        return bans.asSequence()
                .drop(offset)
                .take(count)
                .map(dtoFactory)
                .toList()
    }

    fun getUserWarnsDtoByNickname(nickname: String, offset: Int, count: Int, dtoFactory: (Warn) -> WarnDto): List<WarnDto> {
        val warns = findUserByNickname(nickname).warns ?: return emptyList()
        return warns.asSequence()
                .drop(offset)
                .take(count)
                .map(dtoFactory)
                .toList()
    }

    fun getUserPostsDtoByNickname(nickname: String, offset: Int, count: Int, withDeleted: Boolean, dtoFactory: (Post) -> PostDto): List<PostDto> {
        val posts = findUserByNickname(nickname).posts ?: return emptyList()
        return posts.asSequence()
                .filter {
                    withDeleted || it.deletedBy == null
                }
                .drop(offset)
                .take(count)
                .map(dtoFactory)
                .toList()
    }

    fun getUserCommentDtoByNickname(nickname: String, offset: Int, count: Int, withDeleted: Boolean, dtoFactory: (Comment) -> CommentDto): List<CommentDto> {
        val comments = findUserByNickname(nickname).comments ?: return emptyList()
        return comments.asSequence()
                .filter {
                    withDeleted || it.deletedBy == null
                }
                .drop(offset)
                .take(count)
                .map(dtoFactory)
                .toList()
    }

    fun releaseNewToken(user: User) {
        val editedUser = user.copy(
                lastTokenRelease = LocalDateTime.now()
        )
        userRepository.save(editedUser)
    }

    fun changePassword(user: User, changePasswordRequest: ChangePasswordRequest) {
        if (bCryptPasswordEncoder.matches(changePasswordRequest.oldPassword, user.password)) {
            changePassword(user, changePasswordRequest.newPassword)
        } else {
            throw UserAuthException("Invalid password.")
        }
    }

    fun banUserByNickname(nickname: String, invoker: User, banRequest: BanRequest): Ban {
        if (invoker.activationToken != null) {
            throw UserAuthException("Account inactive.")
        }
        val user = findUserByNickname(nickname)
        val ban = Ban(
                reason = banRequest.reason,
                expireAt = LocalDateTime.now().plusHours(banRequest.durationInHours),
                target = user,
                invoker = invoker
        )
        return banRepository.save(ban)
    }

    fun warnUserByNickname(nickname: String, invoker: User, warnRequest: WarnRequest): Warn {
        if (invoker.activationToken != null) {
            throw UserAuthException("Account inactive.")
        }
        val user = findUserByNickname(nickname)
        val warn = Warn(
                reason = warnRequest.reason,
                target = user,
                invoker = invoker
        )
        return warnRepository.save(warn)
    }

    fun findUserByUsername(username: String): User? {
        return userRepository.findUserByUsername(username)
    }


    @Transactional
    fun register(registerRequest: RegisterRequest): JwtAuthResponse {

        if (userRepository.findUserByEmail(registerRequest.email) != null) {
            throw ResourceAlreadyExistException("Email already exist.")
        }

        if (userRepository.findUserByUsername(registerRequest.username) != null) {
            throw ResourceAlreadyExistException("Username already exist.")
        }

        if (userRepository.findUserByNickname(registerRequest.nickname) != null) {
            throw ResourceAlreadyExistException("Nickname already exist.")
        }

        val role = roleRepository.findRoleByName(RoleName.ROLE_USER)
                ?: throw ResourceNotFoundException("Role not found.")

        val user = User(
                nickname = registerRequest.nickname,
                username = registerRequest.username,
                email = registerRequest.email,
                password = bCryptPasswordEncoder.encode(registerRequest.password),
                role = role
        )

        val newUser = userRepository.save(user)

        newUser.activationToken?.let {
            emailService.sendConfirmationEmail(newUser, it)
        }

        return createAuthResponse(newUser)
    }

    fun login(loginRequest: LoginRequest): JwtAuthResponse {

        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password))

        val user = findUserByUsername(loginRequest.username) ?: throw UserAuthException("User not found")

        return createAuthResponse(user)

    }

    private fun createAuthResponse(user: User): JwtAuthResponse {
        return JwtAuthResponse(
                accessToken = jwtTokenProvider.createToken(user.username, user.lastTokenRelease),
                isAdmin = user.role.name == RoleName.ROLE_ADMIN,
                nickname = user.nickname,
                isActive = user.activationToken == null
        )
    }

    private fun findUserByNickname(nickname: String): User {
        return userRepository.findUserByNickname(nickname) ?: throw ResourceNotFoundException("User not found.")
    }


    fun activeAccount(user: User, activeRequest: ActiveRequest) {
        if (user.activationToken == activeRequest.code) {
            val editedUser = user.copy(
                    activationToken = null
            )
            userRepository.save(editedUser)
        } else {
            throw UserAuthException("Bad token.")
        }

    }

    private fun changePassword(user: User, newPassword: String) {
        val editedUser = user.copy(
                password = bCryptPasswordEncoder.encode(newPassword),
                lastTokenRelease = LocalDateTime.now()
        )
        userRepository.save(editedUser)
    }


    fun resetPassword(confirmResetPasswordRequest: ConfirmResetPasswordRequest) {

        val passwordResetToken = passwordResetTokenRepository.findPasswordResetTokenByCode(confirmResetPasswordRequest.code)
                ?: throw ResourceNotFoundException("User not found")

        val user = passwordResetToken.target

        if (user.email != confirmResetPasswordRequest.usernameOrEmail && user.username != confirmResetPasswordRequest.usernameOrEmail) {
            throw ResourceNotFoundException("User not found")
        }

        if (passwordResetToken.expireAt < LocalDateTime.now()) {
            throw ResourceNotFoundException("Code expired")
        }

        val newPassword = RandomString.make(8)
        changePassword(user, newPassword)

        passwordResetTokenRepository.delete(passwordResetToken)
        emailService.sendNewPassword(user, newPassword)
    }

    fun sendResetPasswordEmail(resetPasswordRequest: ResetPasswordRequest) {
        val user = userRepository.findUserByUsernameOrEmail(resetPasswordRequest.usernameOrEmail, resetPasswordRequest.usernameOrEmail)
                ?: throw ResourceNotFoundException("User not found")

        val oldToken = passwordResetTokenRepository.findPasswordResetTokensByTarget(user)
        oldToken?.asIterable()?.let {
            passwordResetTokenRepository.deleteAll(it)
        }

        val token = PasswordResetToken(
                code = ThreadLocalRandom.current().nextInt(10000, 99999),
                expireAt = LocalDateTime.now().plusHours(2),
                target = user
        )

        val savedToken = passwordResetTokenRepository.save(token)

        emailService.sendResetPasswordRequest(user, savedToken.code)
    }

    @Transactional
    fun addFeedback(nickname: String, feedbackRequest: FeedbackRequest, author: User) {
        val user = findUserByNickname(nickname)

        if (userFeedbackRepository.findUserFeedbackByAuthorAndTarget(author, user) == null) {
            val feedback = UserFeedback(
                    author = author,
                    isPositive = feedbackRequest.like,
                    target = user
            )
            userFeedbackRepository.save(feedback)

        } else {
            throw ResourceAlreadyExistException("You already vote.")
        }
    }

}