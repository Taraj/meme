package tk.tarajki.meme.dto.models



sealed class PostDto {

    data class Basic(
            val name: String
    ) : PostDto()

    data class Extended(
            val id: Long,
            val name: String
    ) : PostDto()

}

