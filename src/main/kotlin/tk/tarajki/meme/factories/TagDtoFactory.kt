package tk.tarajki.meme.factories

import org.springframework.stereotype.Component
import tk.tarajki.meme.dto.models.BanDto
import tk.tarajki.meme.dto.models.TagDto
import tk.tarajki.meme.models.Tag
import kotlin.reflect.KFunction

@Component
class TagDtoFactory {

    fun getTagDto(tag: Tag, kind: KFunction<TagDto>): TagDto {
        return when (kind) {
            BanDto::Basic -> getBasicTagDto(tag)
            else -> getBasicTagDto(tag)
        }
    }

    private fun getBasicTagDto(tag: Tag): TagDto.Basic {
        return TagDto.Basic(
                id = tag.id,
                name = tag.name,
                postsCount = tag.posts?.size ?: 0
        )
    }
}