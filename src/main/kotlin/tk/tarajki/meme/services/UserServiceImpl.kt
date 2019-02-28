package tk.tarajki.meme.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tk.tarajki.meme.models.User
import tk.tarajki.meme.repositories.UserRepository
import javax.persistence.Id

@Service
class UserServiceImpl : UserService {

    @Autowired
    lateinit var userRepository: UserRepository

    override fun findUserById(id: Long): User? {
        return userRepository.findUserById(id)
    }

    override fun findUserByLogin(login: String): User? {
        return userRepository.findUserByLogin(login)
    }

    override fun findAll(): List<User>? {
        return userRepository.findAll()
    }
}