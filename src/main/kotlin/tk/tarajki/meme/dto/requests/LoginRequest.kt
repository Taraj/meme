package tk.tarajki.meme.dto.requests


import javax.validation.constraints.NotEmpty


data class LoginRequest(
        @field:NotEmpty
        val username: String,

        @field:NotEmpty
        val password: String
)