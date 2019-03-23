package tk.tarajki.meme.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import tk.tarajki.meme.dto.models.CommentFeedbackDto
import tk.tarajki.meme.dto.requests.FeedbackRequest
import tk.tarajki.meme.security.UserPrincipal
import tk.tarajki.meme.services.CommentService
import javax.validation.Valid


@CrossOrigin
@RestController
@RequestMapping("/api/v1/comments")
class CommentController(
        private val commentService: CommentService
) {

    @PostMapping("/{id}/feedback")
    fun addFeedback(
            @PathVariable id: Long,
            @RequestBody  @Valid feedbackRequest: FeedbackRequest,
            @AuthenticationPrincipal principal: UserPrincipal
    ): ResponseEntity<Unit> {
        commentService.addFeedback(id, feedbackRequest, principal.user)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @GetMapping("/{id}/feedback")
    fun getFeedback(
            @PathVariable id: Long,
            @RequestParam("offset", defaultValue = "0") offset: Int,
            @RequestParam("count", defaultValue = "10") count: Int
    ): List<CommentFeedbackDto> {
        return commentService.getAllCommentsFeedbackDtoByCommentId(id, offset, count, CommentFeedbackDto::Basic)
    }

}