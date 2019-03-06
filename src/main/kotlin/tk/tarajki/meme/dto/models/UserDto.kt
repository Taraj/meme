package tk.tarajki.meme.dto.models

import tk.tarajki.meme.models.User
import java.time.LocalDateTime

sealed class UserDto {

    data class Basic(
            val nickname: String,
            val avatar: String,
            val isBaned: Boolean,
            val joinedAt: LocalDateTime,
            val commentsCount: Int,
            val postsCount: Int
    ) : UserDto() {
        constructor(user: User) : this(
                nickname = user.nickname,
                avatar = user.avatarURL,
                isBaned = user.bans?.any {
                    it.expireAt > LocalDateTime.now()
                } ?: false,
                joinedAt = user.createdAt,
                commentsCount = user.comments?.size ?: 0,
                postsCount = user.posts?.size ?: 0
        )
    }

    data class Extended(
            val id: Long,
            val username: String,
            val nickname: String,
            val email: String,
            val avatar: String,
            val isBaned: Boolean,
            val joinedAt: LocalDateTime,
            val commentsCount: Int,
            val postsCount: Int
    ) : UserDto() {
        constructor(user: User) : this(
                id = user.id,
                username = user.username,
                nickname = user.nickname,
                email = user.email,
                avatar = user.avatarURL,
                isBaned = user.bans?.any {
                    it.expireAt > LocalDateTime.now()
                } ?: false,
                joinedAt = user.createdAt,
                commentsCount = user.comments?.size ?: 0,
                postsCount = user.posts?.size ?: 0
        )
    }

}



