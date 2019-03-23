package tk.tarajki.meme.models

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
data class Comment(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @field:NotBlank
        @Column(nullable = false)
        val content: String,

        @JoinColumn
        @ManyToOne(fetch = FetchType.LAZY)
        val deletedBy: User? = null,

        @ManyToOne(fetch = FetchType.LAZY)
        val post: Post,

        @ManyToOne(fetch = FetchType.LAZY)
        val author: User,

        @DateTimeFormat
        @Column(nullable = false)
        val createdAt: LocalDateTime = LocalDateTime.now()
)