package tk.tarajki.meme.factories

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tk.tarajki.meme.dto.models.BanDto
import tk.tarajki.meme.dto.models.UserDto
import tk.tarajki.meme.models.Ban
import kotlin.reflect.KFunction
@Component
class BanDtoFactory {

    @Autowired
    lateinit var userDtoFactory: UserDtoFactory

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
                target = userDtoFactory.getUserDto(ban.target, UserDto::Extended),
                invoker = userDtoFactory.getUserDto(ban.invoker, UserDto::Extended),
                createdAt = ban.createdAt
        )
    }

}