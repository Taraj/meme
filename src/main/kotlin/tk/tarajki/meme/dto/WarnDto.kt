package tk.tarajki.meme.dto

import java.util.*

data class WarnDto(
        val id: Long,

        val readAt: Date?,

        val reason: String,

        val target: UserDto,

        var invoker: UserDto,

        var createdAt: Date
)