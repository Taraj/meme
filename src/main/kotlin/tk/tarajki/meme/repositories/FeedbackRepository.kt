package tk.tarajki.meme.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import tk.tarajki.meme.models.Feedback

@Repository
interface FeedbackRepository : JpaRepository<Feedback, Long> {
    fun findByAuthorIp(ip: String): Feedback?
}