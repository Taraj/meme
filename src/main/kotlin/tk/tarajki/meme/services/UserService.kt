package tk.tarajki.meme.services

import tk.tarajki.meme.models.User
import javax.persistence.Id

interface UserService {
    fun findUserByLogin(login: String): User?
    fun findUserById(id: Long): User?
    fun findAll(): List<User>?
}
