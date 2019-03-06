package tk.tarajki.meme.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import tk.tarajki.meme.dto.JwtAuthResponse
import tk.tarajki.meme.dto.requests.LoginRequest
import tk.tarajki.meme.dto.requests.RegisterRequest
import tk.tarajki.meme.exceptions.UserRegisterException
import tk.tarajki.meme.services.UserService
import java.lang.Exception
import javax.validation.Valid


@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
        val userService: UserService
) {


    @PostMapping("/login")
    fun login(@RequestBody @Valid loginRequest: LoginRequest): ResponseEntity<JwtAuthResponse> {
        return try {
            val token = userService.login(loginRequest)
            val jwtAuthResponse = JwtAuthResponse(token)
            ResponseEntity(jwtAuthResponse, HttpStatus.OK)
        } catch (e: BadCredentialsException) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password.")
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }

    }


    @PostMapping("/register")
    fun register(@RequestBody @Valid registerRequest: RegisterRequest): ResponseEntity<Nothing> {
        return try {
            userService.register(registerRequest)
            ResponseEntity(HttpStatus.CREATED)
        } catch (e: UserRegisterException) {
            throw ResponseStatusException(HttpStatus.CONFLICT, e.message)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }
    }

}