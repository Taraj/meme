package tk.tarajki.meme.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import tk.tarajki.meme.dto.models.*
import tk.tarajki.meme.dto.requests.BanRequest
import tk.tarajki.meme.dto.requests.WarnRequest
import tk.tarajki.meme.models.RoleName
import tk.tarajki.meme.security.UserPrincipal
import tk.tarajki.meme.services.UserService


@RestController
@RequestMapping("/api/v1/users")
class UserController(
        private val userService: UserService
) {


    @GetMapping("/", "")
    fun getAllUsers(@AuthenticationPrincipal principal: UserPrincipal?): List<UserDto>? {
        return when (principal?.getRole()) {
            RoleName.ROLE_ADMIN -> userService.findAll()?.asSequence()?.map { UserDto.Extended(it) }?.toList()
            else -> userService.findAll()?.asSequence()?.map { UserDto.Basic(it) }?.toList()
        }
    }

    @GetMapping("/{nickname}")
    fun getUserByNickname(@PathVariable nickname: String, @AuthenticationPrincipal principal: UserPrincipal?): UserDto? {
        val user = userService.findUserByNickname(nickname)
        return when (principal?.getRole()) {
            RoleName.ROLE_ADMIN -> UserDto.Extended(user)
            else -> UserDto.Basic(user)
        }
    }

    @PostMapping("/{nickname}/bans")
    fun banUser(@PathVariable nickname: String, @AuthenticationPrincipal principal: UserPrincipal, @RequestBody banRequest: BanRequest): ResponseEntity<Nothing> {
        val user = userService.findUserByNickname(nickname)
        userService.banUser(user, principal.user, banRequest.reason, banRequest.durationInHours)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @GetMapping("/{nickname}/bans")
    fun getBans(@PathVariable nickname: String): List<BanDto>? {
        val user = userService.findUserByNickname(nickname)
        return user.bans?.asSequence()?.map {
            BanDto.Basic(it)
        }?.toList()
    }

    @PostMapping("/{nickname}/warns")
    fun warnUser(@PathVariable nickname: String, @AuthenticationPrincipal principal: UserPrincipal, @RequestBody warnRequest: WarnRequest): ResponseEntity<Nothing> {
        val user = userService.findUserByNickname(nickname)
        userService.warnUser(user, principal.user, warnRequest.reason)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @GetMapping("/{nickname}/warns")
    fun getWarns(@PathVariable nickname: String): List<WarnDto>? {
        val user = userService.findUserByNickname(nickname)
        return user.warns?.asSequence()?.map {
            WarnDto.Basic(it)
        }?.toList()
    }

    @GetMapping("/{nickname}/posts")
    fun getPosts(@PathVariable nickname: String, @AuthenticationPrincipal principal: UserPrincipal?): List<PostDto>? {
        val user = userService.findUserByNickname(nickname)
        return when (principal?.getRole()) {
            RoleName.ROLE_ADMIN -> user.posts?.asSequence()?.map {
                PostDto.Extended(it)
            }?.toList()
            else -> user.posts?.asSequence()?.filter {
                it.deletedBy == null
            }?.map {
                PostDto.Basic(it)
            }?.toList()
        }
    }

    @GetMapping("/{nickname}/comments")
    fun getComments(@PathVariable nickname: String, @AuthenticationPrincipal principal: UserPrincipal?): List<CommentDto>? {
        val user = userService.findUserByNickname(nickname)
        return when (principal?.getRole()) {
            RoleName.ROLE_ADMIN -> user.comments?.asSequence()?.map {
                CommentDto.Extended(it)
            }?.toList()
            else -> user.comments?.asSequence()?.filter {
                it.deletedBy == null
            }?.map {
                CommentDto.Basic(it)
            }?.toList()
        }
    }
}