package tk.tarajki.meme.services

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import tk.tarajki.meme.dto.requests.LoginRequest
import tk.tarajki.meme.dto.requests.RegisterRequest
import tk.tarajki.meme.exceptions.UserAuthException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.transaction.annotation.Transactional
import tk.tarajki.meme.dto.JwtAuthResponse
import tk.tarajki.meme.dto.models.*
import tk.tarajki.meme.dto.requests.BanRequest
import tk.tarajki.meme.dto.requests.WarnRequest
import tk.tarajki.meme.exceptions.ResourceNotFoundException
import tk.tarajki.meme.models.*
import tk.tarajki.meme.repositories.*
import tk.tarajki.meme.security.JwtTokenProvider
import java.time.LocalDateTime


@Service
class UserService(
        private val userRepository: UserRepository,
        private val roleRepository: RoleRepository,
        private val bCryptPasswordEncoder: BCryptPasswordEncoder,
        private val authenticationManager: AuthenticationManager,
        private val jwtTokenProvider: JwtTokenProvider,
        private val banRepository: BanRepository,
        private val warnRepository: WarnRepository
) {

    fun getAllUsersDto(offset: Int, count: Int, dtoFactory: (User) -> UserDto): List<UserDto> {
        val users = userRepository.findAll()
        return users.asSequence()
                .map(dtoFactory)
                .drop(offset)
                .take(count)
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
                ?.map(dtoFactory)
                ?.drop(offset)
                ?.take(count)
                ?.toList() ?: emptyList()
    }

    fun getUserWarnsDtoByNickname(nickname: String, offset: Int, count: Int, dtoFactory: (Warn) -> WarnDto): List<WarnDto> {
        val user = findUserByNickname(nickname)
        return user.warns
                ?.asSequence()
                ?.map(dtoFactory)
                ?.drop(offset)
                ?.take(count)
                ?.toList() ?: emptyList()
    }

    fun getUserPostsDtoByNickname(nickname: String, offset: Int, count: Int, withDeleted: Boolean, dtoFactory: (Post) -> PostDto): List<PostDto> {
        val user = findUserByNickname(nickname)
        return user.posts
                ?.asSequence()
                ?.filter {
                    withDeleted || it.deletedBy == null
                }
                ?.map(dtoFactory)
                ?.drop(offset)
                ?.take(count)
                ?.toList() ?: emptyList()
    }

    fun getUserCommentsDtoByNickname(nickname: String, offset: Int, count: Int, withDeleted: Boolean, dtoFactory: (Comment) -> CommentDto): List<CommentDto> {
        val user = findUserByNickname(nickname)
        return user.comments
                ?.asSequence()
                ?.filter {
                    withDeleted || it.deletedBy == null
                }
                ?.map(dtoFactory)
                ?.drop(offset)
                ?.take(count)
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

        userRepository.save(user)

        return createAuthResponse(registerRequest.username)
    }

    fun login(loginRequest: LoginRequest): JwtAuthResponse {

        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password))

        return createAuthResponse(loginRequest.username)

    }

    private fun createAuthResponse(username: String): JwtAuthResponse {
        val user = userRepository.findUserByUsername(username) ?: throw UserAuthException("User not found")
        return JwtAuthResponse(
                accessToken = jwtTokenProvider.createToken(user.username),
                isAdmin = user.role.name == RoleName.ROLE_ADMIN,
                nickname = user.nickname
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


}