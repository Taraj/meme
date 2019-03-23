package tk.tarajki.meme.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import tk.tarajki.meme.models.Comment
import tk.tarajki.meme.models.CommentFeedback
import tk.tarajki.meme.models.User


@Repository
interface CommentFeedbackRepository : JpaRepository<CommentFeedback, Long> {
    fun findCommentFeedbackByAuthorAndTarget(user: User, target: Comment): CommentFeedback?
}