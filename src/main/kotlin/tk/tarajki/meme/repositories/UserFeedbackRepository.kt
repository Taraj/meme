package tk.tarajki.meme.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import tk.tarajki.meme.models.UserFeedback

@Repository
interface UserFeedbackRepository : JpaRepository<UserFeedback, Long>