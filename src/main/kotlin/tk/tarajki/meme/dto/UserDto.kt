package tk.tarajki.meme.dto

import java.util.*


sealed class UserDto {
    data class UserDtoRoleGuest(
            val nickname: String,
            val avatar: String,
            val isBaned: Boolean,
            val joinedAt: Date,
            val commentsCount: Int,
            val postsCount: Int,
            val karma: Int

    ) : UserDto()

    data class UserDtoRoleUser(
            val nickname: String,
            val avatar: String,
            val isBaned: Boolean,
            val joinedAt: Date,
            val commentsCount: Int,
            val postsCount: Int,
            val karma: Int

    ) : UserDto()

    data class UserDtoRoleAdmin(
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





