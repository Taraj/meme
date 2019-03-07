package tk.tarajki.meme.dto.requests

import javax.validation.constraints.NotBlank

data class CommentRequest(

        @field:NotBlank
        val content: String

)