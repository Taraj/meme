package tk.tarajki.meme.services


import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tk.tarajki.meme.dto.models.CommentDto
import tk.tarajki.meme.dto.models.PostDto
import tk.tarajki.meme.dto.models.TagDto
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


@Service
class PostService(
        private val postRepository: PostRepository,
        private val commentRepository: CommentRepository,
        private val tagRepository: TagRepository,
        private val feedbackRepository: FeedbackRepository
) {


    fun getAllTagDto(offset: Int, count: Int, dtoFactory: (Tag) -> TagDto): List<TagDto> {
        val tags = tagRepository.findAll()
        return tags.asSequence()
                .map(dtoFactory)
                .drop(offset)
                .take(count)
                .toList()
    }

    fun getAllPostsDto(offset: Int, count: Int, confirmed: Boolean, withDeleted: Boolean, dtoFactory: (Post) -> PostDto): List<PostDto> {
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

    fun getAllPostsDtoByTagName(tagName: String, offset: Int, count: Int, confirmed: Boolean, withDeleted: Boolean, dtoFactory: (Post) -> PostDto): List<PostDto> {
       val tag = tagRepository.getTagByName(tagName)
        return tag?.posts?.asSequence()
                ?.filter {
                    if (confirmed) {
                        it.confirmedBy != null
                    } else {
                        it.confirmedBy == null
                    }
                }
                ?.filter {
                    withDeleted || it.deletedBy == null
                }
                ?.drop(offset)
                ?.take(count)
                ?.map(dtoFactory)
                ?.toList()?: emptyList()
    }


    fun getPostDto(id: Long, withDeleted: Boolean, dtoFactory: (Post) -> PostDto): PostDto {
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

    fun getAllCommentsDtoByPostId(id: Long, offset: Int, count: Int, withDeleted: Boolean, dtoFactory: (Comment) -> CommentDto): List<CommentDto> {
        val post = postRepository.findPostById(id) ?: throw ResourceNotFoundException("Post not found.")
        return post.comments?.asSequence()
                ?.filter {
                    withDeleted || it.deletedBy == null
                }?.map(dtoFactory)
                ?.toList() ?: listOf()
    }

    private fun getOrCreateTagByName(name: String): Tag {
        return tagRepository.getTagByName(name) ?: tagRepository.save(Tag(
                name = name
        ))
    }

    @Transactional
    fun addFeedback(id: Long, feedbackRequest: FeedbackRequest, ip: String): Feedback {
        val post = postRepository.findPostById(id) ?: throw ResourceNotFoundException("Post not found.")
        //if (feedbackRepository.findByAuthorIp(ip) == null) {
        val feedback = Feedback(
                authorIp = ip,
                isPositive = feedbackRequest.like,
                post = post
        )
        return feedbackRepository.save(feedback)
        //    }
        throw ResourceAlreadyExist("You already vote")
    }


}