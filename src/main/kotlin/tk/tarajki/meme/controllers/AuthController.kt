package tk.tarajki.meme.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tk.tarajki.meme.dto.JwtAuthResponse
import tk.tarajki.meme.dto.requests.ConfirmResetPasswordRequest
import tk.tarajki.meme.dto.requests.LoginRequest
import tk.tarajki.meme.dto.requests.RegisterRequest
import tk.tarajki.meme.dto.requests.ResetPasswordRequest
import tk.tarajki.meme.services.UserService
import javax.validation.Valid


@CrossOrigin
@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
        private val userService: UserService
) {

    @PostMapping("/login")
    fun login(
            @RequestBody @Valid loginRequest: LoginRequest
    ): ResponseEntity<JwtAuthResponse> {
        val jwtAuthResponse = userService.login(loginRequest)
        return ResponseEntity(jwtAuthResponse, HttpStatus.OK)
    }

    @PostMapping("/register")
    fun register(
            @RequestBody @Valid registerRequest: RegisterRequest
    ): ResponseEntity<JwtAuthResponse> {
        val jwtAuthResponse = userService.register(registerRequest)
        return ResponseEntity(jwtAuthResponse, HttpStatus.CREATED)
    }

    @PostMapping("/reset")
    fun sendResetPasswordEmail(
            @RequestBody @Valid resetPasswordRequest: ResetPasswordRequest
    ): ResponseEntity<Unit> {
        userService.sendResetPasswordEmail(resetPasswordRequest)
        return ResponseEntity(HttpStatus.ACCEPTED)
    }

    @PostMapping("/reset/confirm")
    fun confirmPasswordReset(
            @RequestBody @Valid confirmResetPasswordRequest: ConfirmResetPasswordRequest
    ): ResponseEntity<Unit> {
        userService.resetPassword(confirmResetPasswordRequest)
        return ResponseEntity(HttpStatus.ACCEPTED)
    }

}