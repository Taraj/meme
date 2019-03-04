package tk.tarajki.meme.dto.requests

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class RegisterRequest(

        @NotBlank
        @Size(min = 3, max = 32)
        val nickname: String,

        @NotBlank
        @Size(min = 3, max = 32)
        val username: String,

        @NotBlank
        @Email
        @Size(min = 3, max = 32)
        val email: String,

        @NotBlank
        @Size(min = 3, max = 32)
        val password: String
)