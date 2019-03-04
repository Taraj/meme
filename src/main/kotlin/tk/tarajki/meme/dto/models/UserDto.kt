package tk.tarajki.meme.dto.models

import java.util.*


sealed class UserDto {

    data class Basic(
            val nickname: String,
            val avatar: String,
            val isBaned: Boolean,
            val joinedAt: Date,
            val commentsCount: Int,
            val postsCount: Int,
            val karma: Int

    ) : UserDto()

    data class Extended(
            val id: Long,
            val username: String,
            val nickname: String,
            val email: String,
            val avatar: String,
            val isBaned: Boolean,
            val joinedAt: Date,
            val commentsCount: Int,
            val postsCount: Int,
            val karma: Int

    ) : UserDto()

}


