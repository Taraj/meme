package tk.tarajki.meme.models

import java.time.LocalDateTime
import javax.persistence.*


@Entity
data class PostFeedback(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @Column(nullable = false)
        val authorIp: String,

        @Column(nullable = false)
        val isPositive: Boolean,

        @ManyToOne(fetch = FetchType.LAZY)
        val target: Post,

        @Column(nullable = false)
        val createdAt: LocalDateTime = LocalDateTime.now()
)