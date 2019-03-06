package tk.tarajki.meme.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import tk.tarajki.meme.dto.models.CommentDto
import tk.tarajki.meme.dto.models.PostDto
import tk.tarajki.meme.dto.requests.CommentRequest
import tk.tarajki.meme.dto.requests.PostRequest
import tk.tarajki.meme.exceptions.ResourceNotFoundException
import tk.tarajki.meme.models.RoleName
import tk.tarajki.meme.security.UserPrincipal
import tk.tarajki.meme.services.PostService

@RestController
@RequestMapping("/api/v1/posts")
class PostController(
        private val postService: PostService
) {


    @GetMapping("/", "")
    fun getAllPosts(@AuthenticationPrincipal principal: UserPrincipal?): List<PostDto>? {
        val posts = postService.findAll()
        return when (principal?.getRole()) {
            RoleName.ROLE_ADMIN -> posts?.map {
                PostDto.Extended(it)
            }
            else -> posts?.filter {
                it.deletedBy == null
            }?.map {
                PostDto.Basic(it)
            }
        }
    }

    @GetMapping("/{id}")
    fun getPostById(@PathVariable id: Long, @AuthenticationPrincipal principal: UserPrincipal?): PostDto? {
        val post = postService.findPostById(id)
        return when (principal?.getRole()) {
            RoleName.ROLE_ADMIN -> PostDto.Extended(post)
            else -> if (post.deletedBy == null) {
                PostDto.Basic(post)
            } else {
                throw ResourceNotFoundException("Post deleted.")
            }
        }
    }

    @DeleteMapping("/{id}")
    fun deletePostById(@PathVariable id: Long, @AuthenticationPrincipal principal: UserPrincipal): ResponseEntity<Nothing> {
        val post = postService.findPostById(id)
        postService.delete(post, principal.user)
        return ResponseEntity(HttpStatus.OK)
    }

    @PutMapping("/{id}")
    fun acceptPostById(@PathVariable id: Long, @AuthenticationPrincipal principal: UserPrincipal): ResponseEntity<Nothing> {
        val post = postService.findPostById(id)
        postService.accept(post, principal.user)
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping("/{id}/comments")
    fun getPostComments(@PathVariable id: Long, @AuthenticationPrincipal principal: UserPrincipal?): List<CommentDto>? {
        val post = postService.findPostById(id)
        return when (principal?.getRole()) {
            RoleName.ROLE_ADMIN -> post.comments?.map {
                CommentDto.Extended(it)
            }
            else -> post.comments?.filter {
                it.deletedBy == null
            }?.map {
                CommentDto.Basic(it)
            }
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
        postService.addComment(post, commentRequest, principal.user)
        return ResponseEntity(HttpStatus.CREATED)
    }

}