package tk.tarajki.meme.factories


import org.springframework.stereotype.Component
import tk.tarajki.meme.dto.models.PostDto
import tk.tarajki.meme.dto.models.TagDto
import tk.tarajki.meme.dto.models.UserDto
import tk.tarajki.meme.models.Post
import kotlin.reflect.KFunction

@Component
class PostDtoFactory(
        private val userDtoFactory: UserDtoFactory,
        private val tagDtoFactory: TagDtoFactory
) {


    fun getPostDto(post: Post, kind: KFunction<PostDto>): PostDto {
        return when (kind) {
            PostDto::Basic -> getBasicPostDto(post)
            PostDto::Extended -> getExtendedPostDto(post)
            else -> getBasicPostDto(post)
        }
    }


    private fun getBasicPostDto(post: Post): PostDto.Basic {
        return PostDto.Basic(
                id = post.id,
                title = post.title,
                url = post.url,
                tags = post.tags.map {
                    tagDtoFactory.getTagDto(it, TagDto::Basic)
                },
                author = userDtoFactory.getUserDto(post.author, UserDto::Basic),
                commentsCount = post.comments?.size ?: 0,
                createdAt = post.createdAt
        )
    }


    private fun getExtendedPostDto(post: Post): PostDto.Extended {
        return PostDto.Extended(
                id = post.id,
                title = post.title,
                url = post.url,
                tags = post.tags.map {
                    tagDtoFactory.getTagDto(it, TagDto::Basic)
                },
                author = userDtoFactory.getUserDto(post.author, UserDto::Extended),
                confirmedBy = if (post.confirmedBy != null) {
                    userDtoFactory.getUserDto(post.confirmedBy, UserDto::Extended)
                } else {
                    null
                },
                deletedBy = if (post.deletedBy != null) {
                    userDtoFactory.getUserDto(post.deletedBy, UserDto::Extended)
                } else {
                    null
                },
                commentsCount = post.comments?.size ?: 0,
                createdAt = post.createdAt
        )
    }
}