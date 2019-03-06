package tk.tarajki.meme.dto.models

import tk.tarajki.meme.models.Warn
import java.time.LocalDateTime


sealed class WarnDto {

    data class Basic(
            val id: Long,
            val readAt: LocalDateTime?,
            val reason: String,
            val target: UserDto,
            val invoker: UserDto,
            val createdAt: LocalDateTime
    ) : WarnDto() {
        constructor(warn: Warn) : this(
                id = warn.id,
                reason = warn.reason,
                readAt = warn.readAt,
                target = UserDto.Extended(warn.target),
                invoker = UserDto.Extended(warn.invoker),
                createdAt = warn.createdAt
        )
    }

}
