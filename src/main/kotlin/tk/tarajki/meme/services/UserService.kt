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
import tk.tarajki.meme.exceptions.ResourceAlreadyExist
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

    fun getUserDtoByNickname(nickname: String, dtoFactory: (User) -> UserDto): UserDto {
        val user = findUserByNickname(nickname)
        return dtoFactory(user)
    }

    fun getUserBansDtoByNickname(nickname: String, offset: Int, count: Int, dtoFactory: (Ban) -> BanDto): List<BanDto> {
        val user = findUserByNickname(nickname)
        return user.bans
                ?.asSequence()
                ?.drop(offset)
                ?.take(count)
                ?.map(dtoFactory)
                ?.toList() ?: emptyList()
    }

    fun getUserWarnsDtoByNickname(nickname: String, offset: Int, count: Int, dtoFactory: (Warn) -> WarnDto): List<WarnDto> {
        val user = findUserByNickname(nickname)
        return user.warns
                ?.asSequence()
                ?.drop(offset)
                ?.take(count)
                ?.map(dtoFactory)
                ?.toList() ?: emptyList()
    }

    fun getUserPostsDtoByNickname(nickname: String, offset: Int, count: Int, withDeleted: Boolean, dtoFactory: (Post) -> PostDto): List<PostDto> {
        val user = findUserByNickname(nickname)
        return user.posts
                ?.asSequence()
                ?.filter {
                    withDeleted || it.deletedBy == null
                }
                ?.drop(offset)
                ?.take(count)
                ?.map(dtoFactory)
                ?.toList() ?: emptyList()
    }

    fun getUserCommentDtoByNickname(nickname: String, offset: Int, count: Int, withDeleted: Boolean, dtoFactory: (Comment) -> CommentDto): List<CommentDto> {
        val user = findUserByNickname(nickname)
        return user.comments
                ?.asSequence()
                ?.filter {
                    withDeleted || it.deletedBy == null
                }
                ?.drop(offset)
                ?.take(count)
                ?.map(dtoFactory)
                ?.toList() ?: emptyList()
    }


    fun banUserByNickname(nickname: String, invoker: User, banRequest: BanRequest): Ban {
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

    fun changePassword(user: User, changePasswordRequest: ChangePasswordRequest) {
        if (bCryptPasswordEncoder.matches(changePasswordRequest.oldPassword, user.password)) {
            changePassword(user, changePasswordRequest.newPassword)
        } else {
            throw UserAuthException("Bad password")
        }
    }

    @Transactional
    fun register(registerRequest: RegisterRequest): JwtAuthResponse {

        if (!isUniqueUserRegisterDto(registerRequest)) {
            throw UserAuthException("Invalid User.")
        }
        val role = roleRepository.findRoleByName(RoleName.ROLE_USER) ?: throw UserAuthException("Bad role.")

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


        return createAuthResponse(newUser.username)
    }

    fun login(loginRequest: LoginRequest): JwtAuthResponse {

        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password))

        return createAuthResponse(loginRequest.username)

    }

    private fun createAuthResponse(username: String): JwtAuthResponse {
        val user = userRepository.findUserByUsername(username) ?: throw UserAuthException("User not found")
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

    private fun isUniqueUserRegisterDto(registerRequest: RegisterRequest): Boolean {

        if (!isUniqueEmail(registerRequest.email)) {
            throw UserAuthException("Email already exist.")
        }

        if (!isUniqueUsername(registerRequest.username)) {
            throw UserAuthException("Username already exist.")
        }

        if (!isUniqueNickname(registerRequest.nickname)) {
            throw UserAuthException("Nickname already exist.")
        }

        return true
    }

    private fun isUniqueEmail(email: String): Boolean {
        return userRepository.findUserByEmail(email) == null
    }

    fun isUniqueNickname(nickname: String): Boolean {
        return userRepository.findUserByNickname(nickname) == null
    }

    private fun isUniqueUsername(username: String): Boolean {
        return userRepository.findUserByUsername(username) == null
    }

    fun activeAccount(user: User, activeRequest: ActiveRequest) {
        if (user.activationToken == activeRequest.code) {
            userRepository.save(user.copy(
                    activationToken = null
            ))
        } else {
            throw UserAuthException("Bad token")
        }

    }

    private fun changePassword(user: User, newPassword: String) {
        userRepository.save(
                userRepository.save(user.copy(
                        password = bCryptPasswordEncoder.encode(newPassword),
                        lastTokenRelease = LocalDateTime.now()
                ))
        )
    }


    fun resetPassword(confirmResetPasswordRequest: ConfirmResetPasswordRequest) {
        val passwordResetToken = passwordResetTokenRepository.findPasswordResetTokenByCode(confirmResetPasswordRequest.code)
                ?: throw ResourceNotFoundException("Not found")

        if (passwordResetToken.expireAt < LocalDateTime.now()) {
            throw ResourceNotFoundException("code expired")
        }
        val user = passwordResetToken.target

        val newPassword = RandomString.make(8)
        changePassword(user, newPassword)

        passwordResetTokenRepository.delete(passwordResetToken)
        emailService.sendNewPassword(user, newPassword)
    }

    fun sendResetPasswordEmail(resetPasswordRequest: ResetPasswordRequest) {
        val user = userRepository.findUserByUsernameOrEmail(resetPasswordRequest.usernameOrEmail, resetPasswordRequest.usernameOrEmail)
                ?: throw ResourceNotFoundException("User not found")

        val passwordResetToken = passwordResetTokenRepository.save(
                PasswordResetToken(
                        code = ThreadLocalRandom.current().nextInt(1000, 9999),
                        expireAt = LocalDateTime.now().plusHours(2),
                        target = user
                )
        )

        emailService.sendResetPasswordRequest(user, passwordResetToken.code)
    }

    @Transactional
    fun addFeedback(nickname: String, feedbackRequest: FeedbackRequest, author: User) {
        val user = userRepository.findUserByNickname(nickname) ?: throw ResourceNotFoundException("user not found")

        if (userFeedbackRepository.findUserFeedbackByAuthorAndTarget(author, user) == null) {
            userFeedbackRepository.save(
                    UserFeedback(
                            author = author,
                            isPositive = feedbackRequest.like,
                            target = user
                    )
            )
        } else {
            throw ResourceAlreadyExist("You already vote")
        }
    }

    fun getAllUserFeedbackDtoByNickname(nickname: String,  offset: Int, count: Int,dtoFactory: (UserFeedback) -> UserFeedbackDto): List<UserFeedbackDto> {
        val userFeedback = userFeedbackRepository.findAll()
        return userFeedback.asSequence()
                .drop(offset)
                .take(count)
                .map(dtoFactory)
                .toList()
    }
}