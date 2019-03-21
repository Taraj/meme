package tk.tarajki.meme.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import tk.tarajki.meme.models.CommentFeedback

@Repository
interface CommentFeedbackRepository : JpaRepository<CommentFeedback, Long>