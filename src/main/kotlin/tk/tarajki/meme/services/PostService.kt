package tk.tarajki.meme.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tk.tarajki.meme.models.Post
import tk.tarajki.meme.models.User
import tk.tarajki.meme.repositories.PostRepository

@Service
class PostService {


    @Autowired
    private lateinit var postRepository: PostRepository

    fun getAllUserPost(user: User): List<Post>? {
        return postRepository.findPostsByAuthor(user)
    }
}