package tk.tarajki.meme.services

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import tk.tarajki.meme.dto.models.PostDto
import tk.tarajki.meme.dto.models.TagDto
import tk.tarajki.meme.models.Post
import tk.tarajki.meme.models.Tag
import tk.tarajki.meme.repositories.TagRepository

@Component
class TagService(
        private val tagRepository: TagRepository
) {
    @Transactional
    fun getOrCreateTag(name: String): Tag {
        return tagRepository.getTagByName(name) ?: tagRepository.save(
                Tag(
                        name = name
                )
        )
    }

    fun getAllPostsDtoByTagName(tagName: String, offset: Int, count: Int, confirmed: Boolean, withDeleted: Boolean, dtoFactory: (Post) -> PostDto): List<PostDto> {
        val tag = tagRepository.getTagByName(tagName)
        val posts = tag?.posts ?: return emptyList()

        return posts.asSequence()
                .filter {
                    if (confirmed) {
                        it.confirmedBy != null
                    } else {
                        it.confirmedBy == null
                    }
                }
                .filter {
                    withDeleted || it.deletedBy == null
                }
                .drop(offset)
                .take(count)
                .map(dtoFactory)
                .toList()
    }


    fun getAllTagDto(offset: Int, count: Int, dtoFactory: (Tag) -> TagDto): List<TagDto> {
        val tags = tagRepository.findAll()
        return tags.asSequence()
                .map(dtoFactory)
                .drop(offset)
                .take(count)
                .toList()
    }
}