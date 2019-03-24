package tk.tarajki.meme.dto.requests

import javax.validation.constraints.NotBlank

data class ConfirmResetPasswordRequest(
        @field:NotBlank
        val usernameOrEmail: String,

        val code: Int
)