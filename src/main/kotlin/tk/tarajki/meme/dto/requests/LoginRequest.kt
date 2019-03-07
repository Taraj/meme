package tk.tarajki.meme.dto.requests


import javax.validation.constraints.NotBlank


data class LoginRequest(
        @field:NotBlank
        val username: String,

        @field:NotBlank
        val password: String
)