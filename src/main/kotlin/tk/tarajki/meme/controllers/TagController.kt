package tk.tarajki.meme.controllers

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import tk.tarajki.meme.dto.models.PostDto
import tk.tarajki.meme.dto.models.TagDto
import tk.tarajki.meme.models.RoleName
import tk.tarajki.meme.security.UserPrincipal
import tk.tarajki.meme.services.PostService

@RestController
@RequestMapping("/api/v1/tags")
class TagController(
        private val postService: PostService
) {

    @GetMapping("/", "")
    fun getAllTags(
            @RequestParam("offset", defaultValue = "0") offset: Int,
            @RequestParam("count", defaultValue = "10") count: Int,
            @RequestParam("confirmed", defaultValue = "true") confirmed: Boolean
    ): List<TagDto> {
        return postService.getAllTagDto(offset, count, TagDto::Basic)
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
            RoleName.ROLE_ADMIN -> postService.getAllPostsDtoByTagName(name, offset, count, confirmed, true, PostDto::Extended)
            else -> postService.getAllPostsDtoByTagName(name, offset, count, confirmed, false, PostDto::Basic)
        }
    }
}
