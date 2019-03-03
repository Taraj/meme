package tk.tarajki.meme.factories

import tk.tarajki.meme.dto.WarnDto
import tk.tarajki.meme.models.RoleName
import tk.tarajki.meme.models.Warn

object WarnDtoFactory {

    fun getWarnDto(warn: Warn):WarnDto{
        return WarnDto(
                id = warn.id,
                reason = warn.reason,
                readAt = warn.readAt,
                target = UserDtoFactory.getUserDto(warn.target, RoleName.ROLE_ADMIN),
                invoker = UserDtoFactory.getUserDto(warn.invoker, RoleName.ROLE_ADMIN),
                createdAt = warn.createdAt
        )
    }
}