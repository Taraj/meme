package tk.tarajki.meme.dto.requests

import org.hibernate.validator.constraints.URL

data class SetAvatarRequest(
        @field:URL
        val avatarURL: String
)