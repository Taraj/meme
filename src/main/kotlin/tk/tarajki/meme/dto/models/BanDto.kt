package tk.tarajki.meme.dto.models

import java.util.*


sealed class BanDto {

    data class Basic(
            val id: Long,
            val expireAt: Date,
            val reason: String,
            val target: UserDto,
            var invoker: UserDto,
            var createdAt: Date
    ) : BanDto()

}


