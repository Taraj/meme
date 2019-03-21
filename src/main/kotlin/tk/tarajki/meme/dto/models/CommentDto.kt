package tk.tarajki.meme.dto.models

import tk.tarajki.meme.models.PostComment
import java.time.LocalDateTime

sealed class CommentDto {

    data class Basic(
            val id: Long,
            val content: String,
            val post: PostDto,
            val author: UserDto,
            val createdAt: LocalDateTime
    ) : CommentDto() {
        constructor(comment: PostComment) : this(
                id = comment.id,
                content = comment.content,
                post = PostDto.Extended(comment.post),
                author = UserDto.Extended(comment.author),
                createdAt = comment.createdAt
        )
    }

    data class Extended(
            val id: Long,
            val content: String,
            val post: PostDto,
            val author: UserDto,
            val deletedBy: UserDto?,
            val createdAt: LocalDateTime
    ) : CommentDto() {
        constructor(comment: PostComment) : this(
                id = comment.id,
                content = comment.content,
                post = PostDto.Extended(comment.post),
                author = UserDto.Extended(comment.author),
                deletedBy = comment.deletedBy?.let {
                    UserDto.Extended(it)
                },
                createdAt = comment.createdAt
        )
    }
}