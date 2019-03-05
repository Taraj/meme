package tk.tarajki.meme.factories


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tk.tarajki.meme.dto.models.UserDto
import tk.tarajki.meme.dto.models.WarnDto
import tk.tarajki.meme.models.Warn
import kotlin.reflect.KFunction

@Component
class WarnDtoFactory {

    @Autowired
    private lateinit var userDtoFactory: UserDtoFactory

    fun getWarnDto(warn: Warn, kind: KFunction<WarnDto>): WarnDto {
        return when (kind) {
            WarnDto::Basic -> getBasicWarnDto(warn)
            else -> getBasicWarnDto(warn)
        }
    }

    private fun getBasicWarnDto(warn: Warn): WarnDto.Basic {
        return WarnDto.Basic(
                id = warn.id,
                reason = warn.reason,
                readAt = warn.readAt,
                target = userDtoFactory.getUserDto(warn.target, UserDto::Extended),
                invoker = userDtoFactory.getUserDto(warn.invoker, UserDto::Extended),
                createdAt = warn.createdAt
        )
    }

}