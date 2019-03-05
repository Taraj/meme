package tk.tarajki.meme.dto.models

import java.util.*


sealed class PostDto {

    data class Basic(
            val id: Long,
            val title: String,
            val url: String,
            val tags: List<TagDto>,
            val author: UserDto,
            val commentsCount: Int,
            val createdAt: Date
    ) : PostDto()

    data class Extended(
            val id: Long,
            val title: String,
            val url: String,
            val tags: List<TagDto>,
            val author: UserDto,
            val confirmedBy: UserDto?,
            val deletedBy: UserDto?,
            val commentsCount: Int,
            val createdAt: Date
    ) : PostDto()

}

