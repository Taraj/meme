package tk.tarajki.meme.dto.models

import java.time.LocalDateTime
import java.util.*


sealed class WarnDto {

    data class Basic(
            val id: Long,
            val readAt: LocalDateTime?,
            val reason: String,
            val target: UserDto,
            var invoker: UserDto,
            var createdAt: LocalDateTime
    ) : WarnDto()

}
