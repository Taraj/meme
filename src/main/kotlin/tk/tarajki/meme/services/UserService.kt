package tk.tarajki.meme.services

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import tk.tarajki.meme.dto.requests.LoginRequest
import tk.tarajki.meme.dto.requests.RegisterRequest
import tk.tarajki.meme.exceptions.UserRegisterException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.transaction.annotation.Transactional
import tk.tarajki.meme.models.*
import tk.tarajki.meme.repositories.*
import tk.tarajki.meme.security.JwtTokenProvider
import tk.tarajki.meme.util.Duration

import java.util.*


@Service
class UserService(
        val userRepository: UserRepository,
        val roleRepository: RoleRepository,
        val bCryptPasswordEncoder: BCryptPasswordEncoder,
        val authenticationManager: AuthenticationManager,
        val jwtTokenProvider: JwtTokenProvider,
        val banRepository: BanRepository,
        val warnRepository: WarnRepository
) {


    fun findUserByUsername(username: String): User? {
        return userRepository.findUserByUsername(username)
    }

    fun findUserByNickname(nickname: String): User? {
        return userRepository.findUserByNickname(nickname)
    }

    fun findAll(): List<User>? {
        return userRepository.findAll()
    }

    fun banUser(target: User, invoker: User, reason: String, duration: Duration): Ban {
        val ban = Ban(
                reason = reason,
                expireAt = duration + Date(),
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
    fun register(registerRequest: RegisterRequest): User? {

        if (!isUniqueUserRegisterDto(registerRequest)) {
            throw UserRegisterException("Invalid User.")
        }
        val role = roleRepository.findRoleByName(RoleName.ROLE_USER) ?: throw UserRegisterException("Bad role.")

        val user = User(
                nickname = registerRequest.nickname,
                username = registerRequest.username,
                email = registerRequest.email,
                password = bCryptPasswordEncoder.encode(registerRequest.password),
                role = role

        )

        return userRepository.save(user)
    }

    fun login(loginRequest: LoginRequest): String {

        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password))

        return jwtTokenProvider.createToken(loginRequest.username)

    }

    private fun isUniqueUserRegisterDto(registerRequest: RegisterRequest): Boolean {

        if (!isUniqueEmail(registerRequest.email)) {
            throw UserRegisterException("Email already exist.")
        }

        if (!isUniqueUsername(registerRequest.username)) {
            throw UserRegisterException("Username already exist.")
        }

        if (!isUniqueNickname(registerRequest.nickname)) {
            throw UserRegisterException("Nickname already exist.")
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