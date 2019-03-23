package tk.tarajki.meme.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import tk.tarajki.meme.models.Post
import tk.tarajki.meme.models.PostFeedback

@Repository
interface PostFeedbackRepository : JpaRepository<PostFeedback, Long> {
    fun findPostFeedbackByAuthorIpAndTarget(ip: String, target: Post): PostFeedback?
}