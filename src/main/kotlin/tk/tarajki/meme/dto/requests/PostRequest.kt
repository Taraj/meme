package tk.tarajki.meme.dto.requests

import javax.validation.constraints.NotBlank

data class PostRequest(

        @NotBlank
        val title: String,

        @NotBlank
        val url: String,

        @NotBlank
        val tags: List<String>
)