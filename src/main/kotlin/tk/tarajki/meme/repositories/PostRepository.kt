package tk.tarajki.meme.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import tk.tarajki.meme.models.Post
import tk.tarajki.meme.models.User

@Repository
interface PostRepository : JpaRepository<Post, Long> {
    fun findPostsByAuthor(author: User): List<Post>?
    fun findPostById(id: Long): Post?
}