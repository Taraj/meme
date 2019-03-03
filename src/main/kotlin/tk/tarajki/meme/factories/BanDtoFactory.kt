package tk.tarajki.meme.factories

import tk.tarajki.meme.dto.BanDto
import tk.tarajki.meme.dto.UserDto
import tk.tarajki.meme.models.Ban
import tk.tarajki.meme.models.RoleName
import java.util.*

object BanDtoFactory {

    fun getBanDto(ban: Ban): BanDto {
        return BanDto(
                id = ban.id,
                expireAt = ban.expireAt,
                reason = ban.reason,
                target = UserDtoFactory.getUserDto(ban.target, RoleName.ROLE_ADMIN),
                invoker = UserDtoFactory.getUserDto(ban.invoker, RoleName.ROLE_ADMIN),
                createdAt = ban.createdAt
        )

    }

}