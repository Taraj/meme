package tk.tarajki.meme.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import tk.tarajki.meme.models.User
import tk.tarajki.meme.security.UserPrincipal
import tk.tarajki.meme.services.UserService


@RestController
@RequestMapping("/api/v1/users")
class UserController {

    @Autowired
    lateinit var userService: UserService

    @GetMapping("/")
    fun getAllUsers(): List<User>? {
        return userService.findAll()
    }

    @GetMapping("/{nickname}")
    fun getUserByNickname(@PathVariable nickname: String): User? {
        return userService.findUserByNickname(nickname)
    }


    @GetMapping("/me")
    fun whoAmI(@AuthenticationPrincipal principal: UserPrincipal?):String{
        return "Jestes ${principal?.username}"
    }

}