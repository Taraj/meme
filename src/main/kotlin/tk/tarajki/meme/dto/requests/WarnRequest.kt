package tk.tarajki.meme.dto.requests

import javax.validation.constraints.NotBlank

data class WarnRequest(

        @NotBlank
        val reason: String
)