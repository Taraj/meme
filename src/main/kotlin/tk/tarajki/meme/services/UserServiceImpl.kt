package tk.tarajki.meme.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import tk.tarajki.meme.dto.UserRegisterDto
import tk.tarajki.meme.exceptions.UserAddException
import tk.tarajki.meme.models.User
import tk.tarajki.meme.repositories.UserRepository
import javax.transaction.Transactional


@Service
class UserServiceImpl : UserService {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var bCryptPasswordEncoder: BCryptPasswordEncoder

    override fun findUserById(id: Long): User? {
        return userRepository.findUserById(id)
    }

    override fun findUserByLogin(login: String): User? {
        return userRepository.findUserByLogin(login)
    }

    override fun findAll(): List<User>? {
        return userRepository.findAll()
    }

    @Transactional
    override fun userAdd(userRegisterDto: UserRegisterDto): User? {

        if (!isValidUserRegisterDto(userRegisterDto)) {
            throw UserAddException("Invalid User.")
        }

        val user = User(
                nickname = userRegisterDto.nickname,
                login = userRegisterDto.login,
                email = userRegisterDto.email,
                password = bCryptPasswordEncoder.encode(userRegisterDto.password)
        )

        return userRepository.save(user)
    }

    private fun isValidUserRegisterDto(userRegisterDto: UserRegisterDto): Boolean {
        if (!isValidEmail(userRegisterDto.email)) {
            throw UserAddException("Invalid Email.")
        }

        if (!isUniqueEmail(userRegisterDto.email)) {
            throw UserAddException("Email already exist.")
        }

        if (!isValidLogin(userRegisterDto.login)) {
            throw UserAddException("Invalid Login.")
        }

        if (!isUniqueLogin(userRegisterDto.login)) {
            throw UserAddException("Login already exist.")
        }

        if (!isValidNickname(userRegisterDto.nickname)) {
            throw UserAddException("Invalid Nickname.")
        }

        if (!isUniqueNickname(userRegisterDto.nickname)) {
            throw UserAddException("Nickname already exist.")
        }

        if (!isValidPassword(userRegisterDto.password)) {
            throw UserAddException("Invalid Password.")
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

    private fun isValidLogin(login: String): Boolean = true

    private fun isUniqueLogin(login: String): Boolean {
        return userRepository.findUserByLogin(login) == null
    }

    private fun isValidPassword(password: String): Boolean = true
}