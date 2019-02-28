package tk.tarajki.meme.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tk.tarajki.meme.models.User
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

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): User? {
        return userService.findUserById(id)
    }

}