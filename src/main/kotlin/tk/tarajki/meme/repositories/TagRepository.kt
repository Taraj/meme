package tk.tarajki.meme.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import tk.tarajki.meme.models.Tag


@Repository
interface TagRepository : JpaRepository<Tag, Long> {
    fun findTagByName(name: String): Tag?
}