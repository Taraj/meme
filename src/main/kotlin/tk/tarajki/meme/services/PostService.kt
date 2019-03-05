package tk.tarajki.meme.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tk.tarajki.meme.dto.requests.CommentRequest
import tk.tarajki.meme.dto.requests.PostRequest
import tk.tarajki.meme.models.Comment
import tk.tarajki.meme.models.Post
import tk.tarajki.meme.models.Tag
import tk.tarajki.meme.models.User
import tk.tarajki.meme.repositories.CommentRepository
import tk.tarajki.meme.repositories.PostRepository
import tk.tarajki.meme.repositories.TagRepository

@Service
class PostService {


    @Autowired
    private lateinit var postRepository: PostRepository

    @Autowired
    private lateinit var commentRepository: CommentRepository


    @Autowired
    private lateinit var tagRepository: TagRepository

    fun findAll(): List<Post>? {
        return postRepository.findAll()

    }

    fun findPostById(id: Long): Post? {
        val po = postRepository.findPostById(id)
        println(po?.comments?.size)
        return po
    }

    fun addComment(post: Post, commentRequest: CommentRequest, author: User): Comment {
        val comment = Comment(
                content = commentRequest.content,
                post = post,
                author = author
        )
        return commentRepository.save(comment)
    }

    fun addPost(postRequest: PostRequest, author: User): Post {
        val post = Post(
                title = postRequest.title,
                url = postRequest.url,
                tags = postRequest.tags.map {
                    getOrCreateTagByName(it)
                },
                author = author
        )
        return postRepository.save(post)
    }

    private fun getOrCreateTagByName(name: String): Tag {
        return tagRepository.getTagByName(name) ?: tagRepository.save(Tag(
                name = name
        ))
    }
}