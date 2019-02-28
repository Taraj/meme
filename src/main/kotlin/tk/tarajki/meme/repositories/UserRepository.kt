package tk.tarajki.meme.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import tk.tarajki.meme.models.User
import javax.persistence.Id

@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun findUserById(id: Long): User?
    fun findUserByLogin(login: String): User?
}