package tk.tarajki.meme.dto.requests

import org.hibernate.validator.constraints.URL
import tk.tarajki.meme.validators.annotations.PostTitle
import tk.tarajki.meme.validators.annotations.TagList

data class PostRequest(

        @field:PostTitle
        val title: String,

        @field:URL
        val url: String,

        @field:TagList
        val tags: List<String>
)