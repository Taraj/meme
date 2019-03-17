package tk.tarajki.meme.services

import org.springframework.context.annotation.Lazy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import tk.tarajki.meme.dto.requests.LoginRequest
import tk.tarajki.meme.dto.requests.RegisterRequest
import tk.tarajki.meme.exceptions.UserAuthException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.transaction.annotation.Transactional
import tk.tarajki.meme.dto.JwtAuthResponse
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
        @Lazy private val authenticationManager: AuthenticationManager,
        private val jwtTokenProvider: JwtTokenProvider,
        private val banRepository: BanRepository,
        private val warnRepository: WarnRepository
) {


    fun findUserByUsername(username: String): User? {
        return userRepository.findUserByUsername(username)
    }

    fun findUserByNickname(nickname: String): User {
        return userRepository.findUserByNickname(nickname) ?: throw ResourceNotFoundException("User not found.")
    }

    fun findAll(): List<User>? {
        return userRepository.findAll()
    }

    fun banUser(target: User, invoker: User, reason: String, durationInHours: Long): Ban {

        val ban = Ban(
                reason = reason,
                expireAt = LocalDateTime.now().plusHours(durationInHours),
                target = target,
                invoker = invoker
        )
        return banRepository.save(ban)
    }

    fun warnUser(target: User, invoker: User, reason: String): Warn {
        val warn = Warn(
                reason = reason,
                target = target,
                invoker = invoker
        )
        return warnRepository.save(warn)
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