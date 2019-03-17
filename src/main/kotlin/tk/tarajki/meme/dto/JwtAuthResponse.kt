package tk.tarajki.meme.dto



data class JwtAuthResponse(
        val accessToken: String,
        val isAdmin: Boolean,
        val nickname: String,
        val tokenType: String = "Bearer"
)