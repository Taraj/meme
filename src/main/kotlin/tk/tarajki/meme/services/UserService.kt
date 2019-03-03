package tk.tarajki.meme.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import tk.tarajki.meme.dto.LoginRequest
import tk.tarajki.meme.dto.RegisterRequest
import tk.tarajki.meme.exceptions.UserRegisterException
import tk.tarajki.meme.models.RoleName
import tk.tarajki.meme.models.User
import tk.tarajki.meme.repositories.RoleRepository
import tk.tarajki.meme.repositories.UserRepository

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.transaction.annotation.Transactional
import tk.tarajki.meme.dto.WarnRequest

import tk.tarajki.meme.models.Ban
import tk.tarajki.meme.models.Warn
import tk.tarajki.meme.repositories.BanRepository
import tk.tarajki.meme.repositories.WarnRepository
import tk.tarajki.meme.security.JwtTokenProvider
import tk.tarajki.meme.util.Duration

import java.util.*


@Service
class UserService {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var roleRepository: RoleRepository

    @Autowired
    private lateinit var bCryptPasswordEncoder: BCryptPasswordEncoder

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @Autowired
    private lateinit var jwtTokenProvider: JwtTokenProvider

    @Autowired
    private lateinit var banRepository: BanRepository

    @Autowired
    private lateinit var warnRepository: WarnRepository

    fun findUserByUsername(username: String): User? {
        return userRepository.findUserByUsername(username)
    }

    @Transactional
    fun findUserByNickname(nickname: String): User? {
        return userRepository.findUserByNickname(nickname)
    }

    fun findAll(): List<User>? {
        return userRepository.findAll()
    }

    fun getUserBans(user: User): List<Ban>? {
        return user.bans
    }

    fun getUserWarns(user: User): List<Warn>? {
        return user.warns
    }

    @Transactional
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