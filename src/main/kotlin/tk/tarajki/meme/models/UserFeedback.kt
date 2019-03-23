package tk.tarajki.meme.models

import java.time.LocalDateTime
import javax.persistence.*


@Entity
data class UserFeedback(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @Column(nullable = false)
        val isPositive: Boolean,

        @ManyToOne(fetch = FetchType.LAZY)
        val author: User,

        @ManyToOne(fetch = FetchType.LAZY)
        val target: User,

        @Column(nullable = false)
        val createdAt: LocalDateTime = LocalDateTime.now()
)