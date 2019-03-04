package tk.tarajki.meme.factories


import tk.tarajki.meme.dto.models.UserDto
import tk.tarajki.meme.dto.models.WarnDto
import tk.tarajki.meme.models.Warn
import kotlin.reflect.KFunction

object WarnDtoFactory {

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
                target = UserDtoFactory.getUserDto(warn.target, UserDto::Extended),
                invoker = UserDtoFactory.getUserDto(warn.invoker, UserDto::Extended),
                createdAt = warn.createdAt
        )
    }

}