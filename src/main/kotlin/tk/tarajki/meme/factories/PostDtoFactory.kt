package tk.tarajki.meme.factories

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tk.tarajki.meme.dto.models.CommentDto
import tk.tarajki.meme.dto.models.PostDto
import tk.tarajki.meme.dto.models.TagDto
import tk.tarajki.meme.dto.models.UserDto
import tk.tarajki.meme.models.Post
import kotlin.reflect.KFunction

@Component
class PostDtoFactory {

    @Autowired
    lateinit var userDtoFactory: UserDtoFactory

    @Autowired
    lateinit var tagDtoFactory: TagDtoFactory

    @Autowired
    lateinit var commentDtoFactory: CommentDtoFactory

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
                comments = post.comments?.map {
                    commentDtoFactory.getCommentDto(it, CommentDto::Basic)
                },
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
                comments = post.comments?.map {
                    commentDtoFactory.getCommentDto(it, CommentDto::Extended)
                },
                createdAt = post.createdAt
        )
    }
}