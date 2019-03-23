package tk.tarajki.meme.controllers

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import tk.tarajki.meme.dto.models.PostDto
import tk.tarajki.meme.dto.models.TagDto
import tk.tarajki.meme.models.RoleName
import tk.tarajki.meme.security.UserPrincipal
import tk.tarajki.meme.services.PostService
import tk.tarajki.meme.services.TagService

@RestController
@RequestMapping("/api/v1/tags")
class TagController(
        private val tagService: TagService
) {

    @GetMapping("/", "")
    fun getAllTags(
            @RequestParam("offset", defaultValue = "0") offset: Int,
            @RequestParam("count", defaultValue = "10") count: Int,
            @RequestParam("confirmed", defaultValue = "true") confirmed: Boolean
    ): List<TagDto> {
        return tagService.getAllTagDto(offset, count, TagDto::Basic)
    }

    @GetMapping("/{name}/posts")
    fun getPostsByTag(
            @PathVariable name: String,
            @AuthenticationPrincipal principal: UserPrincipal?,
            @RequestParam("offset", defaultValue = "0") offset: Int,
            @RequestParam("count", defaultValue = "10") count: Int,
            @RequestParam("confirmed", defaultValue = "true") confirmed: Boolean
    ): List<PostDto> {
        return when (principal?.getRole()) {
            RoleName.ROLE_ADMIN -> tagService.getAllPostsDtoByTagName(name, offset, count, confirmed, true, PostDto::Extended)
            else -> tagService.getAllPostsDtoByTagName(name, offset, count, confirmed, false, PostDto::Basic)
        }
    }
}
