package tk.tarajki.meme.dto.models

import java.util.*


sealed class WarnDto {

    data class Basic(
            val id: Long,
            val readAt: Date?,
            val reason: String,
            val target: UserDto,
            var invoker: UserDto,
            var createdAt: Date
    ) : WarnDto()

}
