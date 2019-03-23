package tk.tarajki.meme.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import tk.tarajki.meme.dto.models.UserDto
import tk.tarajki.meme.dto.models.WarnDto
import tk.tarajki.meme.dto.requests.ActiveRequest
import tk.tarajki.meme.dto.requests.ChangePasswordRequest
import tk.tarajki.meme.models.RoleName
import tk.tarajki.meme.security.UserPrincipal
import tk.tarajki.meme.services.UserService
import javax.validation.Valid

@CrossOrigin
@RestController
@RequestMapping("/api/v1/self")
class SelfController(
        private val userService: UserService
) {
    @GetMapping("/")
    fun whoAmI(@AuthenticationPrincipal principal: UserPrincipal): UserDto {
        return when (principal.getRole()) {
            RoleName.ROLE_ADMIN -> UserDto.Extended(principal.user)
            else -> UserDto.Basic(principal.user)
        }
    }

    @PostMapping("/active")
    fun activeAccount(
            @RequestBody @Valid activeRequest: ActiveRequest,
            @AuthenticationPrincipal principal: UserPrincipal
    ): ResponseEntity<Unit> {
        userService.activeAccount(principal.user, activeRequest)
        return ResponseEntity(HttpStatus.ACCEPTED)
    }

    @PostMapping("/password")
    fun changePassword(
            @RequestBody @Valid changePasswordRequest: ChangePasswordRequest,
            @AuthenticationPrincipal principal: UserPrincipal
    ): ResponseEntity<Unit> {
        userService.changePassword(principal.user, changePasswordRequest)
        return ResponseEntity(HttpStatus.ACCEPTED)
    }

    @GetMapping("/warns")
    fun getWarns(
            @AuthenticationPrincipal principal: UserPrincipal,
            @RequestParam("offset", defaultValue = "0") offset: Int,
            @RequestParam("count", defaultValue = "10") count: Int
    ): List<WarnDto> {
        return userService.getUserWarnsDtoByNickname(principal.user.nickname, offset, count, WarnDto::Basic)
    }

}
