package tk.tarajki.meme.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tk.tarajki.meme.dto.JwtAuthResponse
import tk.tarajki.meme.dto.requests.LoginRequest
import tk.tarajki.meme.dto.requests.RegisterRequest
import tk.tarajki.meme.services.UserService
import javax.validation.Valid


@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
        private val userService: UserService
) {


    @PostMapping("/login")
    fun login(@RequestBody @Valid loginRequest: LoginRequest): ResponseEntity<JwtAuthResponse> {
        val token = userService.login(loginRequest)
        val jwtAuthResponse = JwtAuthResponse(token)
        return ResponseEntity(jwtAuthResponse, HttpStatus.OK)
    }


    @PostMapping("/register")
    fun register(@RequestBody @Valid registerRequest: RegisterRequest): ResponseEntity<Nothing> {
        userService.register(registerRequest)
        return ResponseEntity(HttpStatus.CREATED)
    }

}