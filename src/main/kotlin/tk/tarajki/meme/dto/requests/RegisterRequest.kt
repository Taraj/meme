package tk.tarajki.meme.dto.requests


import tk.tarajki.meme.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class RegisterRequest(


        @field:NotBlank
        @field:Size(min = 3, max = 32)
        val nickname: String,

        @field:NotBlank
        @field:Size(min = 3, max = 32)
        val username: String,

        @field:NotBlank
        @field:Email
        @field:Size(min = 3, max = 32)
        val email: String,

        @field:NotBlank
        @field:Size(min = 3, max = 32)
        val password: String
)