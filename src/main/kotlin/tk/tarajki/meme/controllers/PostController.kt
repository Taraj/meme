package tk.tarajki.meme.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import tk.tarajki.meme.dto.models.CommentDto
import tk.tarajki.meme.dto.models.PostDto
import tk.tarajki.meme.dto.requests.CommentRequest
import tk.tarajki.meme.dto.requests.PostRequest
import tk.tarajki.meme.models.RoleName
import tk.tarajki.meme.security.UserPrincipal
import tk.tarajki.meme.services.PostService
import javax.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestMapping
import tk.tarajki.meme.dto.requests.FeedbackRequest
import javax.servlet.http.HttpServletRequest


@CrossOrigin
@RestController
@RequestMapping("/api/v1/posts")
class PostController(
        private val postService: PostService
) {

    @GetMapping("/", "")
    fun getAllPosts(@AuthenticationPrincipal principal: UserPrincipal?,
                    @RequestParam("offset", defaultValue = "0") offset: Int,
                    @RequestParam("count", defaultValue = "10") count: Int,
                    @RequestParam("confirmed", defaultValue = "false") confirmed: Boolean
    ): List<PostDto> {
        return when (principal?.getRole()) {
            RoleName.ROLE_ADMIN -> postService.getAllExtendedPostsDto(offset, count, confirmed)
            else -> postService.getAllBasicPostsDto(offset, count, confirmed)
        }
    }

    @GetMapping("/{id}")
    fun getPostById(
            @AuthenticationPrincipal principal: UserPrincipal?,
            @PathVariable id: Long
    ): PostDto {
        return when (principal?.getRole()) {
            RoleName.ROLE_ADMIN -> postService.getExtendedPostDto(id)
            else -> postService.getBasicPostDto(id)
        }
    }

    @GetMapping("/random")
    fun getRandomPost(): PostDto {
        return postService.getRandomBasicPostDto()
    }

    @DeleteMapping("/{id}")
    fun deletePostById(
            @AuthenticationPrincipal principal: UserPrincipal,
            @PathVariable id: Long
    ): ResponseEntity<Unit> {
        postService.delete(id, principal.user)
        return ResponseEntity(HttpStatus.OK)
    }

    @PutMapping("/{id}")
    fun acceptPostById(
            @AuthenticationPrincipal principal: UserPrincipal,
            @PathVariable id: Long
    ): ResponseEntity<Unit> {
        postService.accept(id, principal.user)
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping("/{id}/comments")
    fun getPostComments(
            @AuthenticationPrincipal principal: UserPrincipal?,
            @PathVariable id: Long
    ): List<CommentDto>? {
        return when (principal?.getRole()) {
            RoleName.ROLE_ADMIN -> postService.getAllExtendedCommentsDtoByPostId(id)
            else -> postService.getAllBasicCommentsDtoByPostId(id)
        }
    }

    @PostMapping("/")
    fun addNewPost(
            @AuthenticationPrincipal principal: UserPrincipal,
            @RequestBody postRequest: PostRequest
    ): ResponseEntity<Unit> {
        postService.addPost(postRequest, principal.user)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @PostMapping("/{id}/comments")
    fun addNewComment(
            @AuthenticationPrincipal principal: UserPrincipal,
            @PathVariable id: Long,
            @RequestBody commentRequest: CommentRequest
    ): ResponseEntity<Unit> {
        postService.addComment(id, commentRequest, principal.user)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @PostMapping("/{id}/feedback")
    fun addFeedback(
            @PathVariable id: Long,
            @RequestBody feedbackRequest: FeedbackRequest,
            request: HttpServletRequest
    ) {
        postService.addFeedback(id, feedbackRequest, request.remoteAddr)
    }

}