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
import org.springframework.web.bind.annotation.RequestMapping
import tk.tarajki.meme.dto.models.PostFeedbackDto
import tk.tarajki.meme.dto.requests.FeedbackRequest
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid


@CrossOrigin
@RestController
@RequestMapping("/api/v1/posts")
class PostController(
        private val postService: PostService
) {

    @GetMapping("")
    fun getAllPosts(
            @AuthenticationPrincipal principal: UserPrincipal?,
            @RequestParam("offset", defaultValue = "0") offset: Int,
            @RequestParam("count", defaultValue = "10") count: Int,
            @RequestParam("confirmed", defaultValue = "true") confirmed: Boolean
    ): List<PostDto> {
        return when (principal?.getRole()) {
            RoleName.ROLE_ADMIN -> postService.getAllPostDto(offset, count, confirmed, true, PostDto::Extended)
            else -> postService.getAllPostDto(offset, count, confirmed, false, PostDto::Basic)
        }
    }

    @GetMapping("/{id}")
    fun getPostById(
            @AuthenticationPrincipal principal: UserPrincipal?,
            @PathVariable id: Long
    ): PostDto {
        return when (principal?.getRole()) {
            RoleName.ROLE_ADMIN -> postService.getPostDtoByPostId(id, true, PostDto::Extended)
            else -> postService.getPostDtoByPostId(id, false, PostDto::Basic)
        }
    }

    @GetMapping("/random")
    fun getRandomPost(): PostDto {
        return postService.getRandomPostDto(false, PostDto::Basic)
    }

    @DeleteMapping("/{id}")
    fun deletePostById(
            @AuthenticationPrincipal principal: UserPrincipal,
            @PathVariable id: Long
    ): ResponseEntity<Unit> {
        postService.delete(id, principal.user)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @PutMapping("/{id}")
    fun acceptPostById(
            @AuthenticationPrincipal principal: UserPrincipal,
            @PathVariable id: Long
    ): ResponseEntity<Unit> {
        postService.accept(id, principal.user)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @GetMapping("/{id}/comments")
    fun getPostComments(
            @AuthenticationPrincipal principal: UserPrincipal?,
            @PathVariable id: Long,
            @RequestParam("offset", defaultValue = "0") offset: Int,
            @RequestParam("count", defaultValue = "10") count: Int
    ): List<CommentDto> {
        return when (principal?.getRole()) {
            RoleName.ROLE_ADMIN -> postService.getAllCommentDtoByPostId(id, offset, count, true, CommentDto::Extended)
            else -> postService.getAllCommentDtoByPostId(id, offset, count, false, CommentDto::Basic)
        }
    }

    @PostMapping("/")
    fun addNewPost(
            @AuthenticationPrincipal principal: UserPrincipal,
            @RequestBody @Valid postRequest: PostRequest
    ): ResponseEntity<Unit> {
        postService.addPost(postRequest, principal.user)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @PostMapping("/{id}/comments")
    fun addNewComment(
            @AuthenticationPrincipal principal: UserPrincipal,
            @PathVariable id: Long,
            @RequestBody @Valid commentRequest: CommentRequest
    ): ResponseEntity<Unit> {
        postService.addComment(id, commentRequest, principal.user)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @PostMapping("/{id}/feedback")
    fun addFeedback(
            @PathVariable id: Long,
            @RequestBody @Valid feedbackRequest: FeedbackRequest,
            request: HttpServletRequest
    ): ResponseEntity<Unit> {
        postService.addFeedback(id, feedbackRequest, request.remoteAddr)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @GetMapping("/{id}/feedback")
    fun getFeedback(
            @PathVariable id: Long,
            @RequestParam("offset", defaultValue = "0") offset: Int,
            @RequestParam("count", defaultValue = "10") count: Int
    ): List<PostFeedbackDto> {
        return postService.getAllPostFeedbackDtoByPostId(id, offset, count, PostFeedbackDto::Basic)
    }

}