package tk.tarajki.meme.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tk.tarajki.meme.dto.requests.FeedbackRequest
import tk.tarajki.meme.exceptions.ResourceAlreadyExist
import tk.tarajki.meme.exceptions.ResourceNotFoundException
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
        val comment = commentRepository.findCommentById(id) ?: throw ResourceNotFoundException("Comment not found")

        if (commentFeedbackRepository.findCommentFeedbackByAuthorAndTarget(author, comment) == null) {
            commentFeedbackRepository.save(
                    CommentFeedback(
                            author = author,
                            isPositive = feedbackRequest.like,
                            target = comment
                    )
            )
        } else {
            throw ResourceAlreadyExist("You already vote")
        }
    }
}