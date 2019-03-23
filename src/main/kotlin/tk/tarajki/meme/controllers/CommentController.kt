package tk.tarajki.meme.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import tk.tarajki.meme.dto.requests.FeedbackRequest
import tk.tarajki.meme.security.UserPrincipal
import tk.tarajki.meme.services.CommentService


@CrossOrigin
@RestController
@RequestMapping("/api/v1/comments")
class CommentController(
        private val commentService: CommentService
) {

    @PostMapping("/{id}/feedback")
    fun addFeedback(
            @PathVariable id: Long,
            @RequestBody feedbackRequest: FeedbackRequest,
            @AuthenticationPrincipal principal: UserPrincipal
    ): ResponseEntity<Unit> {
        commentService.addFeedback(id, feedbackRequest, principal.user)
        return ResponseEntity(HttpStatus.CREATED)
    }

}