package tk.tarajki.meme.models

import java.time.LocalDateTime
import javax.persistence.*


@Entity
data class Comment(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @Lob
        @Column(nullable = false)
        val content: String,

        @ManyToOne(fetch = FetchType.LAZY)
        val deletedBy: User? = null,

        @ManyToOne(fetch = FetchType.LAZY)
        val post: Post,

        @ManyToOne(fetch = FetchType.LAZY)
        val author: User,

        @Column(nullable = false)
        val createdAt: LocalDateTime = LocalDateTime.now()
)