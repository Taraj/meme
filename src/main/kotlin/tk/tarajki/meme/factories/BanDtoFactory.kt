package tk.tarajki.meme.factories

import tk.tarajki.meme.dto.models.BanDto
import tk.tarajki.meme.dto.models.UserDto
import tk.tarajki.meme.models.Ban
import kotlin.reflect.KFunction

object BanDtoFactory {

    fun getBanDto(ban: Ban, kind: KFunction<BanDto>): BanDto {
        return when (kind) {
            BanDto::Basic -> getBasicBanDto(ban)
            else -> getBasicBanDto(ban)
        }
    }

    private fun getBasicBanDto(ban: Ban): BanDto.Basic {
        return BanDto.Basic(
                id = ban.id,
                expireAt = ban.expireAt,
                reason = ban.reason,
                target = UserDtoFactory.getUserDto(ban.target, UserDto::Extended),
                invoker = UserDtoFactory.getUserDto(ban.invoker, UserDto::Extended),
                createdAt = ban.createdAt
        )
    }

}