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
import tk.tarajki.meme.dto.requests.FeedbackRequest
import javax.servlet.http.HttpServletRequest


@CrossOrigin
@RestController
@RequestMapping("/api/v1/posts")
class PostController(
        private val postService: PostService
) {

    @GetMapping("/", "")
    fun getAllPosts(
            @AuthenticationPrincipal principal: UserPrincipal?,
            @RequestParam("offset", defaultValue = "0") offset: Int,
            @RequestParam("count", defaultValue = "10") count: Int,
            @RequestParam("confirmed", defaultValue = "false") confirmed: Boolean
    ): List<PostDto> {
        return when (principal?.getRole()) {
            RoleName.ROLE_ADMIN -> postService.getAllPostsDto(offset, count, confirmed, true, PostDto::Extended)
            else -> postService.getAllPostsDto(offset, count, confirmed, false, PostDto::Basic)
        }
    }

    @GetMapping("/{id}")
    fun getPostById(
            @AuthenticationPrincipal principal: UserPrincipal?,
            @PathVariable id: Long
    ): PostDto {
        return when (principal?.getRole()) {
            RoleName.ROLE_ADMIN -> postService.getPostDto(id, true, PostDto::Extended)
            else -> postService.getPostDto(id, false, PostDto::Basic)
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
            RoleName.ROLE_ADMIN -> postService.getAllCommentsDtoByPostId(id, true, CommentDto::Extended)
            else -> postService.getAllCommentsDtoByPostId(id, false, CommentDto::Basic)
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
    ): ResponseEntity<Unit> {
        postService.addFeedback(id, feedbackRequest, request.remoteAddr)
        return ResponseEntity(HttpStatus.CREATED)
    }

}