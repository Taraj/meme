package tk.tarajki.meme.factories


import tk.tarajki.meme.dto.models.UserDto
import tk.tarajki.meme.models.User
import java.util.*
import kotlin.reflect.KFunction


object UserDtoFactory {


    fun getUserDto(user: User, kind: KFunction<UserDto>): UserDto {
        return when (kind) {
            UserDto::Basic -> getBasicUserDto(user)
            UserDto::Extended -> getExtendedUserDto(user)
            else -> getBasicUserDto(user)
        }

    }

    private fun getBasicUserDto(user: User): UserDto.Basic {
        return UserDto.Basic(
                nickname = user.nickname,
                avatar = user.avatarURL,
                isBaned = isBaned(user),
                joinedAt = user.createdAt,
                commentsCount = 0,
                postsCount = 0,
                karma = 0
        )
    }

    private fun getExtendedUserDto(user: User): UserDto {
        return UserDto.Extended(
                id = user.id,
                username = user.username,
                nickname = user.nickname,
                email = user.email,
                avatar = user.avatarURL,
                isBaned = isBaned(user),
                joinedAt = user.createdAt,
                commentsCount = 0,
                postsCount = 0,
                karma = 0
        )
    }

    private fun isBaned(user: User): Boolean {
        return user.bans?.any {
            it.expireAt > Date()
        } ?: false

    }
}