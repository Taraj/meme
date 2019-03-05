package tk.tarajki.meme.dto.models


sealed class TagDto {

    data class Basic(
            val id: Long,
            val name: String,
            val posts: List<PostDto>?
    ) : TagDto()

}