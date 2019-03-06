package tk.tarajki.meme.dto.models

import tk.tarajki.meme.models.Ban
import java.time.LocalDateTime


sealed class BanDto {

    data class Basic(
            val id: Long,
            val expireAt: LocalDateTime,
            val reason: String,
            val target: UserDto,
            val invoker: UserDto,
            val createdAt: LocalDateTime
    ) : BanDto() {
        constructor(ban: Ban) : this(
                id = ban.id,
                expireAt = ban.expireAt,
                reason = ban.reason,
                target = UserDto.Extended(ban.target),
                invoker = UserDto.Extended(ban.invoker),
                createdAt = ban.createdAt
        )
    }

}


