package tk.tarajki.meme.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import tk.tarajki.meme.dto.models.*
import tk.tarajki.meme.dto.requests.BanRequest
import tk.tarajki.meme.dto.requests.WarnRequest
import tk.tarajki.meme.factories.*
import tk.tarajki.meme.models.RoleName
import tk.tarajki.meme.security.UserPrincipal
import tk.tarajki.meme.services.UserService


@RestController
@RequestMapping("/api/v1/users")
class UserController(
        val userService: UserService,
        val userDtoFactory: UserDtoFactory,
        val banDtoFactory: BanDtoFactory,
        val warnDtoFactory: WarnDtoFactory,
        val commentDtoFactory: CommentDtoFactory,
        val postDtoFactory: PostDtoFactory
) {


    @GetMapping("/")
    fun getAllUsers(@AuthenticationPrincipal principal: UserPrincipal?): List<UserDto>? {
        return userService.findAll()?.map {
            val kind = when {
                principal?.getRole() == RoleName.ROLE_ADMIN -> UserDto::Extended
                else -> UserDto::Basic
            }
            userDtoFactory.getUserDto(it, kind)
        }
    }

    @GetMapping("/{nickname}")
    fun getUserByNickname(@PathVariable nickname: String, @AuthenticationPrincipal principal: UserPrincipal?): UserDto? {
        val user = userService.findUserByNickname(nickname)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        val kind = when {
            principal?.getRole() == RoleName.ROLE_ADMIN -> UserDto::Extended
            else -> UserDto::Basic
        }
        return userDtoFactory.getUserDto(user, kind)
    }

    @PostMapping("/{nickname}/bans")
    fun banUser(@PathVariable nickname: String, @AuthenticationPrincipal principal: UserPrincipal, @RequestBody banRequest: BanRequest): ResponseEntity<Nothing> {
        val user = userService.findUserByNickname(nickname)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")

        userService.banUser(user, principal.user, banRequest.reason, banRequest.durationInHours)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @GetMapping("/{nickname}/bans")
    fun getBans(@PathVariable nickname: String): List<BanDto>? {
        val user = userService.findUserByNickname(nickname)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        return user.bans?.map {
            banDtoFactory.getBanDto(it, BanDto::Basic)
        }
    }

    @PostMapping("/{nickname}/warns")
    fun warnUser(@PathVariable nickname: String, @AuthenticationPrincipal principal: UserPrincipal, @RequestBody warnRequest: WarnRequest): ResponseEntity<Nothing> {
        val user = userService.findUserByNickname(nickname)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        userService.warnUser(user, principal.user, warnRequest.reason)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @GetMapping("/{nickname}/warns")
    fun getWarns(@PathVariable nickname: String): List<WarnDto>? {
        val user = userService.findUserByNickname(nickname)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        return user.warns?.map {
            warnDtoFactory.getWarnDto(it, WarnDto::Basic)
        }
    }

    @GetMapping("/{nickname}/posts")
    fun getPosts(@PathVariable nickname: String, @AuthenticationPrincipal principal: UserPrincipal?): List<PostDto>? {
        val user = userService.findUserByNickname(nickname)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        val kind = when {
            principal?.getRole() == RoleName.ROLE_ADMIN -> PostDto::Extended
            else -> PostDto::Basic
        }
        return user.posts?.map {
            postDtoFactory.getPostDto(it, kind)
        }
    }

    @GetMapping("/{nickname}/comments")
    fun getComments(@PathVariable nickname: String, @AuthenticationPrincipal principal: UserPrincipal?): List<CommentDto>? {
        val user = userService.findUserByNickname(nickname)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        val kind = when {
            principal?.getRole() == RoleName.ROLE_ADMIN -> CommentDto::Extended
            else -> CommentDto::Basic
        }
        return user.comments?.map {
            commentDtoFactory.getCommentDto(it, kind)
        }
    }
}