package tk.tarajki.meme.dto.requests

import javax.validation.constraints.NotBlank

data class WarnRequest(

        @field:NotBlank
        val reason: String
)