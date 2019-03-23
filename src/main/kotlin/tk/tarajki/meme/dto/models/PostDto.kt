package tk.tarajki.meme.dto.models

import tk.tarajki.meme.models.Post
import java.time.LocalDateTime


sealed class PostDto {

    data class Basic(
            val id: Long,
            val title: String,
            val url: String,
            val tags: List<TagDto>,
            val author: UserDto,
            val commentsCount: Int,
            val likes: Int,
            val dislikes: Int,
            val createdAt: LocalDateTime
    ) : PostDto() {
        constructor(post: Post) : this(
                id = post.id,
                title = post.title,
                url = post.url,
                tags = post.tags.map {
                    TagDto.Basic(it)
                },
                author = UserDto.Basic(post.author),
                commentsCount = post.comments?.size ?: 0,
                likes = post.postFeedback?.count {
                    it.isPositive
                } ?: 0,
                dislikes = post.postFeedback?.count {
                    !it.isPositive
                } ?: 0,
                createdAt = post.createdAt
        )
    }

    data class Extended(
            val id: Long,
            val title: String,
            val url: String,
            val tags: List<TagDto>,
            val author: UserDto,
            val confirmedBy: UserDto?,
            val deletedBy: UserDto?,
            val commentsCount: Int,
            val likes: Int,
            val dislikes: Int,
            val createdAt: LocalDateTime
    ) : PostDto() {
        constructor(post: Post) : this(
                id = post.id,
                title = post.title,
                url = post.url,
                tags = post.tags.map {
                    TagDto.Basic(it)
                },
                author = UserDto.Extended(post.author),
                confirmedBy = post.confirmedBy?.let {
                    UserDto.Extended(it)
                },
                deletedBy = post.deletedBy?.let {
                    UserDto.Extended(it)
                },
                commentsCount = post.comments?.size ?: 0,
                likes = post.postFeedback?.count {
                    it.isPositive
                } ?: 0,
                dislikes = post.postFeedback?.count {
                    !it.isPositive
                } ?: 0,
                createdAt = post.createdAt
        )
    }
}

