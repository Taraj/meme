package tk.tarajki.meme.services


import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tk.tarajki.meme.dto.models.CommentDto
import tk.tarajki.meme.dto.models.PostDto
import tk.tarajki.meme.dto.models.PostFeedbackDto
import tk.tarajki.meme.dto.requests.CommentRequest
import tk.tarajki.meme.dto.requests.FeedbackRequest
import tk.tarajki.meme.dto.requests.PostRequest
import tk.tarajki.meme.exceptions.ResourceAlreadyExist
import tk.tarajki.meme.exceptions.ResourceNotFoundException
import tk.tarajki.meme.exceptions.UserAuthException
import tk.tarajki.meme.models.*
import tk.tarajki.meme.repositories.CommentRepository
import tk.tarajki.meme.repositories.PostFeedbackRepository
import tk.tarajki.meme.repositories.PostRepository
import java.time.LocalDateTime

import java.util.concurrent.ThreadLocalRandom


@Service
class PostService(
        private val postRepository: PostRepository,
        private val commentRepository: CommentRepository,
        private val tagService: TagService,
        private val postFeedbackRepository: PostFeedbackRepository
) {

    fun getAllPostDto(offset: Int, count: Int, confirmed: Boolean, withDeleted: Boolean, dtoFactory: (Post) -> PostDto): List<PostDto> {
        val posts = postRepository.findAll()
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

    fun getPostDtoByPostId(id: Long, withDeleted: Boolean, dtoFactory: (Post) -> PostDto): PostDto {
        val post = postRepository.findPostById(id) ?: throw ResourceNotFoundException("Post not found")

        if (withDeleted) {
            return dtoFactory(post)
        }

        if (post.deletedBy == null) {
            return dtoFactory(post)
        }

        throw ResourceNotFoundException("Post deleted.")
    }


    fun getRandomPostDto(onlyConfirmed: Boolean, dtoFactory: (Post) -> PostDto): PostDto {
        val posts = if (onlyConfirmed) {
            postRepository.findAllByDeletedByIsNullAndConfirmedByIsNotNull()
                    ?: throw ResourceNotFoundException("Post not found.")
        } else {
            postRepository.findAllByDeletedByIsNull() ?: throw ResourceNotFoundException("Post not found.")
        }
        val randomId = ThreadLocalRandom.current().nextInt(posts.size)
        return dtoFactory(posts[randomId])
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
        if (author.activationToken != null)
            throw UserAuthException("Account inactive.")
        val post = postRepository.findPostById(id) ?: throw ResourceNotFoundException("Post not found.")
        val comment = Comment(
                content = commentRequest.content,
                post = post,
                author = author
        )
        return commentRepository.save(comment)
    }

    fun addPost(postRequest: PostRequest, author: User): Post {
        if (author.activationToken != null)
            throw UserAuthException("Account inactive.")
        val post = Post(
                title = postRequest.title,
                url = postRequest.url,
                tags = postRequest.tags.asSequence().map {
                    tagService.getOrCreateTag(it)
                }.toList(),
                author = author
        )
        return postRepository.save(post)
    }

    fun getAllCommentDtoByPostId(id: Long, offset: Int, count: Int, withDeleted: Boolean, dtoFactory: (Comment) -> CommentDto): List<CommentDto> {
        val post = postRepository.findPostById(id) ?: throw ResourceNotFoundException("Post not found.")
        return post.comments?.asSequence()
                ?.filter {
                    withDeleted || it.deletedBy == null
                }?.map(dtoFactory)
                ?.toList() ?: listOf()
    }


    @Transactional
    fun addFeedback(id: Long, feedbackRequest: FeedbackRequest, ip: String) {
        val post = postRepository.findPostById(id) ?: throw ResourceNotFoundException("Post not found.")
        if (postFeedbackRepository.findPostFeedbackByAuthorIpAndTarget(ip, post) == null) {
            postFeedbackRepository.save(PostFeedback(
                    authorIp = ip,
                    isPositive = feedbackRequest.like,
                    target = post
            ))
        } else {
            throw ResourceAlreadyExist("You already vote")
        }
    }

    fun getAllPostFeedbackDtoByPostId(id: Long, offset: Int, count: Int, dtoFactory: (PostFeedback) -> PostFeedbackDto): List<PostFeedbackDto> {
        val postFeedback = postFeedbackRepository.findAll()
        return postFeedback.asSequence()
                .drop(offset)
                .take(count)
                .map(dtoFactory)
                .toList()
    }

}