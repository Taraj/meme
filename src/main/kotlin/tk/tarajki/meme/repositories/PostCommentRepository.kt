package tk.tarajki.meme.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import tk.tarajki.meme.models.PostComment
import tk.tarajki.meme.models.User

@Repository
interface PostCommentRepository : JpaRepository<PostComment, Long> {
    fun findCommentsByAuthor(author: User): List<PostComment>?
    fun countCommentByAuthor(author: User): Int
}