package tk.tarajki.meme.dto.models

import tk.tarajki.meme.models.Warn
import java.time.LocalDateTime


sealed class WarnDto {

    data class Basic(
            val id: Long,
            val reason: String,
            val target: UserDto,
            val createdAt: LocalDateTime
    ) : WarnDto() {
        constructor(warn: Warn) : this(
                id = warn.id,
                reason = warn.reason,
                target = UserDto.Basic(warn.target),
                createdAt = warn.createdAt
        )
    }

    data class Extended(
            val id: Long,
            val reason: String,
            val target: UserDto,
            val invoker: UserDto,
            val createdAt: LocalDateTime
    ) : WarnDto() {
        constructor(warn: Warn) : this(
                id = warn.id,
                reason = warn.reason,
                target = UserDto.Extended(warn.target),
                invoker = UserDto.Extended(warn.invoker),
                createdAt = warn.createdAt
        )
    }

}
