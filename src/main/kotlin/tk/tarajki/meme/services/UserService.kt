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
import javax.transaction.Transactional
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import tk.tarajki.meme.security.JwtTokenProvider


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


    fun findUserByUsername(username: String): User? {
        return userRepository.findUserByUsername(username)
    }

    fun findUserByNickname(nickname: String): User? {
        return userRepository.findUserByNickname(nickname)
    }

    fun findAll(): List<User>? {
        return userRepository.findAll()
    }


    @Transactional
    fun register(registerRequest: RegisterRequest): User? {

        if (!isValidUserRegisterDto(registerRequest)) {
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


    private fun isValidUserRegisterDto(registerRequest: RegisterRequest): Boolean {
        if (!isValidEmail(registerRequest.email)) {
            throw UserRegisterException("Invalid Email.")
        }

        if (!isUniqueEmail(registerRequest.email)) {
            throw UserRegisterException("Email already exist.")
        }

        if (!isValidUsername(registerRequest.username)) {
            throw UserRegisterException("Invalid Username.")
        }

        if (!isUniqueUsername(registerRequest.username)) {
            throw UserRegisterException("Username already exist.")
        }

        if (!isValidNickname(registerRequest.nickname)) {
            throw UserRegisterException("Invalid Nickname.")
        }

        if (!isUniqueNickname(registerRequest.nickname)) {
            throw UserRegisterException("Nickname already exist.")
        }

        if (!isValidPassword(registerRequest.password)) {
            throw UserRegisterException("Invalid Password.")
        }

        return true
    }

    private fun isValidEmail(email: String): Boolean = true

    private fun isUniqueEmail(email: String): Boolean {
        return userRepository.findUserByEmail(email) == null
    }

    private fun isValidNickname(nickname: String): Boolean = true

    fun isUniqueNickname(nickname: String): Boolean {
        return userRepository.findUserByNickname(nickname) == null
    }

    private fun isValidUsername(username: String): Boolean = true

    private fun isUniqueUsername(username: String): Boolean {
        return userRepository.findUserByUsername(username) == null
    }

    private fun isValidPassword(password: String): Boolean = true


}