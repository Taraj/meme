package tk.tarajki.meme.dto.requests


import tk.tarajki.meme.validators.annotations.Nickname
import tk.tarajki.meme.validators.annotations.Password
import tk.tarajki.meme.validators.annotations.Username
import javax.validation.constraints.Email


data class RegisterRequest(

        @field:Nickname
        val nickname: String,

        @field:Username
        val username: String,

        @field:Email
        val email: String,

        @field:Password
        val password: String
)