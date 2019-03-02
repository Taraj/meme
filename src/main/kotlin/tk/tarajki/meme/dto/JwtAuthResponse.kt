package tk.tarajki.meme.dto

import javax.validation.constraints.NotBlank

data class JwtAuthResponse(

        @NotBlank
        val accessToken: String,

        @NotBlank
        val tokenType: String = "Bearer"
)