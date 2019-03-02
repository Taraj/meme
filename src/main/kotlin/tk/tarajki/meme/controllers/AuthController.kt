package tk.tarajki.meme.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import tk.tarajki.meme.dto.JwtAuthResponse
import tk.tarajki.meme.dto.LoginRequest
import tk.tarajki.meme.dto.RegisterRequest
import tk.tarajki.meme.exceptions.UserRegisterException
import tk.tarajki.meme.services.UserService
import java.lang.Exception
import javax.validation.Valid


@RestController
@RequestMapping("/api/v1/auth")
class AuthController {

    @Autowired
    private lateinit var userService: UserService


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