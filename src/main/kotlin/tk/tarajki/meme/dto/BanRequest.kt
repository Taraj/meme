package tk.tarajki.meme.dto

data class BanRequest(
        val reason: String,
        val durationInHours: Double
)
