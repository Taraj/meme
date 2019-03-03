package tk.tarajki.meme.dto

import tk.tarajki.meme.models.User
import java.util.*

data class BanDto(
        val id: Long,

        val expireAt: Date,

        val reason: String,


        val target: UserDto,


        var invoker: UserDto,


        var createdAt: Date
)