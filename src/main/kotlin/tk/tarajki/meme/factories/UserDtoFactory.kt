package tk.tarajki.meme.factories

import tk.tarajki.meme.dto.UserDto
import tk.tarajki.meme.models.RoleName
import tk.tarajki.meme.models.User
import java.util.*


object UserDtoFactory {

    fun getUserDto(user: User, roleName: RoleName?): UserDto {
        return when (roleName) {
            null -> userDtoRoleGuest(user)
            RoleName.ROLE_ADMIN -> userDtoRoleAdmin(user)
            RoleName.ROLE_USER -> userDtoRoleUser(user)
        }
    }

    private fun userDtoRoleGuest(user: User): UserDto {
        return UserDto.UserDtoRoleGuest(
                nickname = user.nickname,
                avatar = user.avatarURL,
                isBaned = isBaned(user),
                joinedAt = user.createdAt,
                commentsCount = 0,
                postsCount = 0,
                karma = 0
        )
    }

    private fun userDtoRoleUser(user: User): UserDto {
        return UserDto.UserDtoRoleUser(
                nickname = user.nickname,
                avatar = user.avatarURL,
                isBaned = isBaned(user),
                joinedAt = user.createdAt,
                commentsCount = 0,
                postsCount = 0,
                karma = 0
        )
    }

    private fun userDtoRoleAdmin(user: User): UserDto {
        return UserDto.UserDtoRoleAdmin(
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