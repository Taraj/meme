package tk.tarajki.meme.dto.models

import java.util.*

sealed class CommentDto {

    data class Basic(
            val id: Long,
            val content: String,
            val post: PostDto,
            var author: UserDto,
            var createdAt: Date
    ) : CommentDto()

    data class Extended(
            val id: Long,
            val content: String,
            val post: PostDto,
            var author: UserDto,
            var deletedBy: UserDto?,
            var createdAt: Date
    ) : CommentDto()
}