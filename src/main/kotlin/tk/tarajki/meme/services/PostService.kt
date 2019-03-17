package tk.tarajki.meme.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tk.tarajki.meme.dto.models.CommentDto
import tk.tarajki.meme.dto.models.PostDto
import tk.tarajki.meme.dto.requests.CommentRequest
import tk.tarajki.meme.dto.requests.FeedbackRequest
import tk.tarajki.meme.dto.requests.PostRequest
import tk.tarajki.meme.exceptions.ResourceAlreadyExist
import tk.tarajki.meme.exceptions.ResourceNotFoundException
import tk.tarajki.meme.models.*
import tk.tarajki.meme.repositories.CommentRepository
import tk.tarajki.meme.repositories.FeedbackRepository
import tk.tarajki.meme.repositories.PostRepository
import tk.tarajki.meme.repositories.TagRepository
import java.time.LocalDateTime
import java.util.concurrent.ThreadLocalRandom
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.primaryConstructor

@Service
class PostService(
        private val postRepository: PostRepository,
        private val commentRepository: CommentRepository,
        private val tagRepository: TagRepository,
        private val feedbackRepository: FeedbackRepository
) {

    fun getAllBasicPostsDto(offset: Int, count: Int, confirmedOnly: Boolean, withDeleted: Boolean = false): List<PostDto> {
        val posts = postRepository.findAll()
        return posts.asSequence()
                .sortedBy(Post::confirmedAt)
                .filter {
                    if (confirmedOnly) {
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
                .map {
                    PostDto.Basic(it)
                }.toList()
    }

    fun getAllExtendedPostsDto(offset: Int, count: Int, confirmedOnly: Boolean, withDeleted: Boolean = true): List<PostDto> {
        val posts = postRepository.findAll()
        return posts.asSequence()
                .sortedBy(Post::confirmedAt)
                .filter {
                    if (confirmedOnly) {
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
                .map {
                    PostDto.Extended(it)
                }.toList()
    }

    fun getBasicPostDto(id: Long, withDeleted: Boolean = false): PostDto {
        val post = postRepository.findPostById(id) ?: throw ResourceNotFoundException("Post not found")

        if (withDeleted) {
            return PostDto.Basic(post)
        }

        if (post.deletedBy == null) {
            return PostDto.Basic(post)
        }

        throw ResourceNotFoundException("Post deleted.")
    }

    fun getExtendedPostDto(id: Long, withDeleted: Boolean = true): PostDto {

        val post = postRepository.findPostById(id) ?: throw ResourceNotFoundException("Post not found")

        if (withDeleted) {
            return PostDto.Extended(post)
        }

        if (post.deletedBy == null) {
            return PostDto.Extended(post)
        }

        throw ResourceNotFoundException("Post deleted.")
    }

    fun getRandomBasicPostDto(onlyConfirmed: Boolean = false): PostDto {
        val posts = if (onlyConfirmed) {
            postRepository.findAllByDeletedByIsNullAndConfirmedByIsNotNull()
                    ?: throw ResourceNotFoundException("Post not found.")
        } else {
            postRepository.findAllByDeletedByIsNull() ?: throw ResourceNotFoundException("Post not found.")
        }
        val randomId = ThreadLocalRandom.current().nextInt(posts.size)
        return PostDto.Basic(posts[randomId])
    }


    fun delete(id: Long, user: User): Post {
        val post = postRepository.findPostById(id) ?: throw ResourceNotFoundException("Post not found.")
        val editedPost = post.copy(
                deletedBy = user,
                deletedAt = LocalDateTime.now()
        )
        return postRepository.save(editedPost)
    }

    fun accept(id: Long, user: User): Post {
        val post = postRepository.findPostById(id) ?: throw ResourceNotFoundException("Post not found.")
        val editedPost = post.copy(
                confirmedBy = user,
                confirmedAt = LocalDateTime.now()
        )
        return postRepository.save(editedPost)
    }


    fun addComment(id: Long, commentRequest: CommentRequest, author: User): Comment {
        val post = postRepository.findPostById(id) ?: throw ResourceNotFoundException("Post not found.")
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

    fun getAllBasicCommentsDtoByPostId(id: Long, withDeleted: Boolean = false): List<CommentDto> {
        val post = postRepository.findPostById(id) ?: throw ResourceNotFoundException("Post not found.")
        return post.comments?.asSequence()
                ?.filter {
                    withDeleted || it.deletedBy == null
                }?.map {
                    CommentDto.Basic(it)
                }?.toList() ?: listOf()
    }

    fun getAllExtendedCommentsDtoByPostId(id: Long, withDeleted: Boolean = true): List<CommentDto> {
        val post = postRepository.findPostById(id) ?: throw ResourceNotFoundException("Post not found.")
        return post.comments?.asSequence()
                ?.filter {
                    withDeleted || it.deletedBy == null
                }?.map {
                    CommentDto.Extended(it)
                }?.toList() ?: listOf()
    }

    private fun getOrCreateTagByName(name: String): Tag {
        return tagRepository.getTagByName(name) ?: tagRepository.save(Tag(
                name = name
        ))
    }

    @Transactional
    fun addFeedback(id: Long, feedbackRequest: FeedbackRequest, ip: String): Feedback {
        val post = postRepository.findPostById(id) ?: throw ResourceNotFoundException("Post not found.")
        if (feedbackRepository.findByAuthorIp(ip) == null) {
            val feedback = Feedback(
                    authorIp = ip,
                    isPositive = feedbackRequest.like,
                    post = post
            )
            return feedbackRepository.save(feedback)
        }
        throw ResourceAlreadyExist("You already vote")
    }


}