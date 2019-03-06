package tk.tarajki.meme.factories

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tk.tarajki.meme.dto.models.CommentDto
import tk.tarajki.meme.dto.models.PostDto
import tk.tarajki.meme.dto.models.UserDto
import tk.tarajki.meme.models.Comment

import kotlin.reflect.KFunction

@Component
class CommentDtoFactory(
        val userDtoFactory: UserDtoFactory,
        val postDtoFactory: PostDtoFactory
) {


    fun getCommentDto(comment: Comment, kind: KFunction<CommentDto>): CommentDto {
        return when (kind) {
            CommentDto::Basic -> getBasicCommentDto(comment)
            CommentDto::Extended -> getExtendedCommentDto(comment)
            else -> getBasicCommentDto(comment)
        }
    }

    private fun getBasicCommentDto(comment: Comment): CommentDto.Basic {
        return CommentDto.Basic(
                id = comment.id,
                content = comment.content,
                post = postDtoFactory.getPostDto(comment.post, PostDto::Basic),
                author = userDtoFactory.getUserDto(comment.author, UserDto::Basic),
                createdAt = comment.createdAt
        )
    }

    private fun getExtendedCommentDto(comment: Comment): CommentDto.Extended {
        return CommentDto.Extended(
                id = comment.id,
                content = comment.content,
                post = postDtoFactory.getPostDto(comment.post, PostDto::Basic),
                author = userDtoFactory.getUserDto(comment.author, UserDto::Basic),
                deletedBy = if (comment.deletedBy != null) {
                    userDtoFactory.getUserDto(comment.deletedBy, UserDto::Extended)
                } else {
                    null
                },
                createdAt = comment.createdAt
        )
    }

}