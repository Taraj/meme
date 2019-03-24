package tk.tarajki.meme.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import tk.tarajki.meme.dto.models.*
import tk.tarajki.meme.dto.requests.BanRequest
import tk.tarajki.meme.dto.requests.FeedbackRequest
import tk.tarajki.meme.dto.requests.WarnRequest
import tk.tarajki.meme.models.RoleName
import tk.tarajki.meme.security.UserPrincipal
import tk.tarajki.meme.services.UserService
import javax.validation.Valid


@CrossOrigin
@RestController
@RequestMapping("/api/v1/users")
class UserController(
        private val userService: UserService
) {
    @GetMapping( "")
    fun getAllUsers(
            @AuthenticationPrincipal principal: UserPrincipal?,
            @RequestParam("offset", defaultValue = "0") offset: Int,
            @RequestParam("count", defaultValue = "10") count: Int
    ): List<UserDto> {
        return when (principal?.getRole()) {
            RoleName.ROLE_ADMIN -> userService.getAllUserDto(offset, count, UserDto::Extended)
            else -> userService.getAllUserDto(offset, count, UserDto::Basic)
        }
    }

    @GetMapping("/{nickname}")
    fun getUserByNickname(
            @PathVariable nickname: String,
            @AuthenticationPrincipal principal: UserPrincipal?
    ): UserDto {
        return when (principal?.getRole()) {
            RoleName.ROLE_ADMIN -> userService.getUserDtoByNickname(nickname, UserDto::Extended)
            else -> userService.getUserDtoByNickname(nickname, UserDto::Basic)
        }
    }

    @PostMapping("/{nickname}/bans")
    fun banUser(
            @PathVariable nickname: String,
            @AuthenticationPrincipal principal: UserPrincipal,
            @RequestBody @Valid banRequest: BanRequest
    ): ResponseEntity<Unit> {
        userService.banUserByNickname(nickname, principal.user, banRequest)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @GetMapping("/{nickname}/bans")
    fun getBans(
            @PathVariable nickname: String,
            @RequestParam("offset", defaultValue = "0") offset: Int,
            @RequestParam("count", defaultValue = "10") count: Int
    ): List<BanDto> {
        return userService.getUserBansDtoByNickname(nickname, offset, count, BanDto::Basic)
    }

    @PostMapping("/{nickname}/warns")
    fun warnUser(
            @PathVariable nickname: String,
            @AuthenticationPrincipal principal: UserPrincipal,
            @RequestBody @Valid warnRequest: WarnRequest
    ): ResponseEntity<Nothing> {
        userService.warnUserByNickname(nickname, principal.user, warnRequest)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @GetMapping("/{nickname}/warns")
    fun getWarns(
            @PathVariable nickname: String,
            @RequestParam("offset", defaultValue = "0") offset: Int,
            @RequestParam("count", defaultValue = "10") count: Int
    ): List<WarnDto> {
        return userService.getUserWarnsDtoByNickname(nickname, offset, count, WarnDto::Extended)
    }

    @GetMapping("/{nickname}/posts")
    fun getPosts(
            @PathVariable nickname: String,
            @AuthenticationPrincipal principal: UserPrincipal?,
            @RequestParam("offset", defaultValue = "0") offset: Int,
            @RequestParam("count", defaultValue = "10") count: Int
    ): List<PostDto> {
        return when (principal?.getRole()) {
            RoleName.ROLE_ADMIN -> userService.getUserPostsDtoByNickname(nickname, offset, count, true, PostDto::Extended)
            else -> userService.getUserPostsDtoByNickname(nickname, offset, count, false, PostDto::Basic)
        }
    }

    @GetMapping("/{nickname}/comments")
    fun getComments(
            @PathVariable nickname: String,
            @AuthenticationPrincipal principal: UserPrincipal?,
            @RequestParam("offset", defaultValue = "0") offset: Int,
            @RequestParam("count", defaultValue = "10") count: Int
    ): List<CommentDto>? {
        return when (principal?.getRole()) {
            RoleName.ROLE_ADMIN -> userService.getUserCommentDtoByNickname(nickname, offset, count, true, CommentDto::Extended)
            else -> userService.getUserCommentDtoByNickname(nickname, offset, count, false, CommentDto::Basic)
        }
    }

    @PostMapping("/{nickname}/feedback")
    fun addFeedback(
            @PathVariable nickname: String,
            @RequestBody @Valid feedbackRequest: FeedbackRequest,
            @AuthenticationPrincipal principal: UserPrincipal
    ): ResponseEntity<Unit> {
        userService.addFeedback(nickname, feedbackRequest, principal.user)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @GetMapping("/{nickname}/feedback")
    fun addFeedback(
            @PathVariable nickname: String,
            @RequestParam("offset", defaultValue = "0") offset: Int,
            @RequestParam("count", defaultValue = "10") count: Int
    ): List<UserFeedbackDto> {
        return userService.getAllUserFeedbackDtoByNickname(nickname, offset, count, UserFeedbackDto::Basic)
    }

}