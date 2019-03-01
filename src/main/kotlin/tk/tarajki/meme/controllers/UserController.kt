package tk.tarajki.meme.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import tk.tarajki.meme.dto.UserRegisterDto
import tk.tarajki.meme.exceptions.UserAddException
import tk.tarajki.meme.models.User
import tk.tarajki.meme.services.UserService
import java.lang.Exception


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

    @PostMapping("/")
    fun userAdd(@RequestBody userRegisterDto: UserRegisterDto): String? {
        return try {
            userService.userAdd(userRegisterDto).toString()
        } catch (userAddException: UserAddException) {
            userAddException.message
        } catch (exception: Exception) {
            exception.toString()
        }
    }

}