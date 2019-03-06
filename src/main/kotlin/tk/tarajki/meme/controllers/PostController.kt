package tk.tarajki.meme.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import tk.tarajki.meme.dto.models.CommentDto
import tk.tarajki.meme.dto.models.PostDto
import tk.tarajki.meme.dto.requests.CommentRequest
import tk.tarajki.meme.dto.requests.PostRequest
import tk.tarajki.meme.factories.CommentDtoFactory
import tk.tarajki.meme.factories.PostDtoFactory
import tk.tarajki.meme.models.RoleName
import tk.tarajki.meme.security.UserPrincipal
import tk.tarajki.meme.services.PostService

@RestController
@RequestMapping("/api/v1/posts")
class PostController(
        val postService: PostService,
        val postDtoFactory: PostDtoFactory,
        val commentDtoFactory: CommentDtoFactory
) {


    @GetMapping("/")
    fun getAllPosts(@AuthenticationPrincipal principal: UserPrincipal?): List<PostDto>? {
        return postService.findAll()?.map {
            val kind = when {
                principal?.getRole() == RoleName.ROLE_ADMIN -> PostDto::Extended
                else -> PostDto::Basic
            }
            postDtoFactory.getPostDto(it, kind)
        }
    }

    @GetMapping("/{id}")
    fun getPostById(@PathVariable id: Long, @AuthenticationPrincipal principal: UserPrincipal?): PostDto? {
        val post = postService.findPostById(id)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found")
        val kind = when {
            principal?.getRole() == RoleName.ROLE_ADMIN -> PostDto::Extended
            else -> PostDto::Basic
        }
        return postDtoFactory.getPostDto(post, kind)
    }

    @Transactional
    @GetMapping("/{id}/comments")
    fun getPostComments(@PathVariable id: Long, @AuthenticationPrincipal principal: UserPrincipal?): List<CommentDto>? {
        val post = postService.findPostById(id)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found")
        val kind = when {
            principal?.getRole() == RoleName.ROLE_ADMIN -> CommentDto::Extended
            else -> CommentDto::Basic
        }

        return post.comments?.map {
            commentDtoFactory.getCommentDto(it, kind)
        }
    }

    @PostMapping("/")
    fun addNewPost(@AuthenticationPrincipal principal: UserPrincipal, @RequestBody postRequest: PostRequest): ResponseEntity<Nothing> {
        postService.addPost(postRequest, principal.user)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @PostMapping("/{id}/comments")
    fun addNewComment(@PathVariable id: Long, @AuthenticationPrincipal principal: UserPrincipal, @RequestBody commentRequest: CommentRequest): ResponseEntity<Nothing> {
        val post = postService.findPostById(id)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found")
        postService.addComment(post, commentRequest, principal.user)
        return ResponseEntity(HttpStatus.CREATED)
    }

}