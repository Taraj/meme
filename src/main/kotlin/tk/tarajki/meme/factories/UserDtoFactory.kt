package tk.tarajki.meme.factories


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tk.tarajki.meme.dto.models.UserDto
import tk.tarajki.meme.models.User
import tk.tarajki.meme.repositories.UserRepository
import java.util.*
import kotlin.reflect.KFunction

@Component
class UserDtoFactory {

    @Autowired
    lateinit var userRepository:UserRepository

    fun getUserDto(user: User, kind: KFunction<UserDto>): UserDto {
        println(userRepository.count())
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
                postsCount = 0

        )
    }

    private fun getExtendedUserDto(user: User): UserDto.Extended {
        return UserDto.Extended(
                id = user.id,
                username = user.username,
                nickname = user.nickname,
                email = user.email,
                avatar = user.avatarURL,
                isBaned = isBaned(user),
                joinedAt = user.createdAt,
                commentsCount = 0,
                postsCount = 0
        )
    }

    private fun isBaned(user: User): Boolean {
        return user.bans?.any {
            it.expireAt > Date()
        } ?: false

    }
}