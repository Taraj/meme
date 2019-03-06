package tk.tarajki.meme.dto.models

import java.time.LocalDateTime
import java.util.*


sealed class BanDto {

    data class Basic(
            val id: Long,
            val expireAt: LocalDateTime,
            val reason: String,
            val target: UserDto,
            var invoker: UserDto,
            var createdAt: LocalDateTime
    ) : BanDto()

}


