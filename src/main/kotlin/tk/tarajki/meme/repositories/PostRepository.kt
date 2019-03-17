package tk.tarajki.meme.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import tk.tarajki.meme.models.Post
import tk.tarajki.meme.models.User

@Repository
interface PostRepository : JpaRepository<Post, Long> {
    fun findPostById(id: Long): Post?
    fun findAllByDeletedByIsNull(): List<Post>?
    fun findAllByDeletedByIsNullAndConfirmedByIsNotNull(): List<Post>?
}