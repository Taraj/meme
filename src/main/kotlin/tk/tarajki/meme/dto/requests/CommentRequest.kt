package tk.tarajki.meme.dto.requests

import tk.tarajki.meme.validators.annotations.Comment


data class CommentRequest(

        @field:Comment
        val content: String

)