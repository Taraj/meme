package tk.tarajki.meme.dto.models

import tk.tarajki.meme.models.Tag


sealed class TagDto {

    data class Basic(
            val id: Long,
            val name: String,
            val postsCount: Int
    ) : TagDto() {
        constructor(tag: Tag) : this(
                id = tag.id,
                name = tag.name,
                postsCount = tag.posts?.size ?: 0
        )
    }

}