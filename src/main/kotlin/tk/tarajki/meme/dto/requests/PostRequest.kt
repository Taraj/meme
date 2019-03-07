package tk.tarajki.meme.dto.requests

import javax.validation.constraints.NotBlank

data class PostRequest(

        @field:NotBlank
        val title: String,

        @field:NotBlank
        val url: String,

        @field:NotBlank
        val tags: List<String>
)