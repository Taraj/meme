package tk.tarajki.meme.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tk.tarajki.meme.dto.models.CommentFeedbackDto
import tk.tarajki.meme.dto.requests.FeedbackRequest
import tk.tarajki.meme.exceptions.ResourceAlreadyExistException
import tk.tarajki.meme.exceptions.ResourceNotFoundException
import tk.tarajki.meme.exceptions.UserAuthException
import tk.tarajki.meme.models.CommentFeedback
import tk.tarajki.meme.models.User
import tk.tarajki.meme.repositories.CommentFeedbackRepository
import tk.tarajki.meme.repositories.CommentRepository

@Service
class CommentService(
        private val commentRepository: CommentRepository,
        private val commentFeedbackRepository: CommentFeedbackRepository
) {
    @Transactional
    fun addFeedback(id: Long, feedbackRequest: FeedbackRequest, author: User) {
        if (author.activationToken != null) {
            throw UserAuthException("Account inactive.")
        }
        val comment = commentRepository.findCommentById(id) ?: throw ResourceNotFoundException("Comment not found.")

        if (commentFeedbackRepository.findCommentFeedbackByAuthorAndTarget(author, comment) == null) {
            commentFeedbackRepository.save(
                    CommentFeedback(
                            author = author,
                            isPositive = feedbackRequest.like,
                            target = comment
                    )
            )
        } else {
            throw ResourceAlreadyExistException("You already vote.")
        }
    }

    fun getAllCommentsFeedbackDtoByCommentId(id: Long, offset: Int, count: Int, dtoFactory: (CommentFeedback) -> CommentFeedbackDto): List<CommentFeedbackDto> {
        val commentsFeedback = commentFeedbackRepository.findAll()
        return commentsFeedback.asSequence()
                .drop(offset)
                .take(count)
                .map(dtoFactory)
                .toList()
    }
}