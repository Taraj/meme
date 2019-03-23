package tk.tarajki.meme.dto.requests

import tk.tarajki.meme.validators.annotations.Password
import javax.validation.constraints.NotBlank

data class ChangePasswordRequest(

        @field:NotBlank
        val oldPassword: String,

        @field:Password
        val newPassword: String
)