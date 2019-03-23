package tk.tarajki.meme.dto.requests

import javax.validation.constraints.NotBlank

data class ResetPasswordRequest(

        @field:NotBlank
        val usernameOrEmail:String
)