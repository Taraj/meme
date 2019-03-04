package tk.tarajki.meme.dto.requests

data class BanRequest(
        val reason: String,
        val durationInHours: Double
)
