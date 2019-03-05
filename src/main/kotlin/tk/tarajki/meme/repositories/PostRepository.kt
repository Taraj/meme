package tk.tarajki.meme.repositories

import org.springframework.data.jpa.repository.JpaRepository
import tk.tarajki.meme.models.Post
import tk.tarajki.meme.models.User

interface PostRepository : JpaRepository<Post, Long> {
    fun findPostsByAuthor(author: User): List<Post>?
}