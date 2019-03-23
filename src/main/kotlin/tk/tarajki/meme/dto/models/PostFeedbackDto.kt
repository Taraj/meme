package tk.tarajki.meme.dto.models

import tk.tarajki.meme.models.PostFeedback
import java.time.LocalDateTime

sealed class PostFeedbackDto {
    data class Basic(
            val id: Long,
            val ip: String,
            val isPositive: Boolean,
            val createdAt: LocalDateTime
    ) : PostFeedbackDto() {
        constructor(postFeedback: PostFeedback) :
                this(
                        id = postFeedback.id,
                        ip = postFeedback.authorIp,
                        isPositive = postFeedback.isPositive,
                        createdAt = postFeedback.createdAt
                )

    }
}