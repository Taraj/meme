package tk.tarajki.meme.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import tk.tarajki.meme.models.Comment
import tk.tarajki.meme.models.User

@Repository
interface CommentRepository : JpaRepository<Comment, Long> {
    fun findCommentsByAuthor(author: User): List<Comment>?
    fun countCommentByAuthor(author: User): Int
}