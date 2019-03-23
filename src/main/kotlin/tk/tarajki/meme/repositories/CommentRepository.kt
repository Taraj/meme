package tk.tarajki.meme.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import tk.tarajki.meme.models.Comment


@Repository
interface CommentRepository : JpaRepository<Comment, Long> {
    fun findCommentById(id: Long): Comment?
}