package tk.tarajki.meme.dto.requests

data class ChangePasswordRequest(
        val oldPassword: String,
        val newPassword: String
)