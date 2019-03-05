package tk.tarajki.meme.dto.requests

import javax.validation.constraints.NotBlank

data class BanRequest(
        @NotBlank
        val reason: String,

        @NotBlank
        val durationInHours: Double
)
