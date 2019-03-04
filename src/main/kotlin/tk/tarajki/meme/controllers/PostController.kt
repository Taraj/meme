package tk.tarajki.meme.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/posts")
class PostController {

    @GetMapping("/")
    fun getAllPosts() {

    }

    @PostMapping("/")
    fun addNewPost(){

    }


}