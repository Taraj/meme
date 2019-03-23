package tk.tarajki.meme.dto.models

import tk.tarajki.meme.models.CommentFeedback
import java.time.LocalDateTime

sealed class CommentFeedbackDto {
    data class Basic(
            val id: Long,
            val author: UserDto,
            val isPositive: Boolean,
            val createdAt: LocalDateTime
    ) : CommentFeedbackDto() {
        constructor(commentFeedback: CommentFeedback) :
                this(
                        id = commentFeedback.id,
                        author = UserDto.Extended(commentFeedback.author),
                        isPositive = commentFeedback.isPositive,
                        createdAt = commentFeedback.createdAt
                )

    }
}