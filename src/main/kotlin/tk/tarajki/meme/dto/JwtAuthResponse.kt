package tk.tarajki.meme.dto


data class JwtAuthResponse(
        val accessToken: String,

        val tokenType: String = "Bearer"
)