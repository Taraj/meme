package tk.tarajki.meme.factories

import tk.tarajki.meme.dto.models.PostDto
import tk.tarajki.meme.models.Post
import kotlin.reflect.KFunction


object PostDtoFactory {


    fun getPostDto(post: Post, kind: KFunction<PostDto>): PostDto {
        return when (kind) {
            PostDto::Basic -> getBasicPostDto(post)
            PostDto::Extended -> getExtendedPostDto(post)
            else -> getBasicPostDto(post)
        }
    }


    private fun getBasicPostDto(post: Post): PostDto.Basic {
        return PostDto.Basic(
                name = post.title
        )
    }

    private fun getExtendedPostDto(post: Post): PostDto.Extended {
        return PostDto.Extended(
                id = post.id,
                name = post.title
        )
    }
}