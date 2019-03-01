package tk.tarajki.meme.dto

data class UserRegisterDto(
        val nickname: String,
        val login: String,
        val email: String,
        val password: String
)