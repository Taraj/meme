package tk.tarajki.meme.services

import org.springframework.stereotype.Service
import tk.tarajki.meme.dto.requests.CommentRequest
import tk.tarajki.meme.dto.requests.PostRequest
import tk.tarajki.meme.exceptions.ResourceNotFoundException
import tk.tarajki.meme.models.Comment
import tk.tarajki.meme.models.Post
import tk.tarajki.meme.models.Tag
import tk.tarajki.meme.models.User
import tk.tarajki.meme.repositories.CommentRepository
import tk.tarajki.meme.repositories.PostRepository
import tk.tarajki.meme.repositories.TagRepository

@Service
class PostService(
        private val postRepository: PostRepository,
        private val commentRepository: CommentRepository,
        private val tagRepository: TagRepository
) {


    fun findAll(): List<Post>? {
        return postRepository.findAll()

    }

    fun delete(post: Post, user: User): Post {
        val editedPost = post.copy(
                deletedBy = user
        )
        return postRepository.save(editedPost)
    }

    fun accept(post: Post, user: User): Post {
        val editedPost = post.copy(
                confirmedBy = user
        )
        return postRepository.save(editedPost)
    }

    fun findPostById(id: Long): Post {
        return postRepository.findPostById(id) ?: throw ResourceNotFoundException("Post not found.")
    }

    fun addComment(post: Post, commentRequest: CommentRequest, author: User): Comment {
        val comment = Comment(
                content = commentRequest.content,
                post = post,
                author = author
        )
        return commentRepository.save(comment)
    }

    fun addPost(postRequest: PostRequest, author: User): Post {
        val post = Post(
                title = postRequest.title,
                url = postRequest.url,
                tags = postRequest.tags.asSequence().map {
                    getOrCreateTagByName(it)
                }.toList(),
                author = author
        )
        return postRepository.save(post)
    }

    private fun getOrCreateTagByName(name: String): Tag {
        return tagRepository.getTagByName(name) ?: tagRepository.save(Tag(
                name = name
        ))
    }
}