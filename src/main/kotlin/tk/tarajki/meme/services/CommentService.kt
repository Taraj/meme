package tk.tarajki.meme.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tk.tarajki.meme.models.Comment
import tk.tarajki.meme.models.User
import tk.tarajki.meme.repositories.CommentRepository

@Service
class CommentService {

    @Autowired
    private lateinit var commentRepository: CommentRepository


    fun getAllUserComments(user: User): List<Comment>? {
        return commentRepository.findCommentsByAuthor(user)
    }
}