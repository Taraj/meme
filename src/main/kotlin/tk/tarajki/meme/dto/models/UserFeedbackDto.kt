package tk.tarajki.meme.dto.models

import tk.tarajki.meme.models.PostFeedback
import tk.tarajki.meme.models.UserFeedback
import java.time.LocalDateTime

sealed class UserFeedbackDto {
    data class Basic(
            val id: Long,
            val author: UserDto,
            val isPositive: Boolean,
            val createdAt: LocalDateTime
    ) : UserFeedbackDto() {
        constructor(userFeedback: UserFeedback) :
                this(
                        id = userFeedback.id,
                        author = UserDto.Extended(userFeedback.author),
                        isPositive = userFeedback.isPositive,
                        createdAt = userFeedback.createdAt
                )

    }
}