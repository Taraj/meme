package tk.tarajki.meme.models

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime
import javax.persistence.*


@Entity
data class UserFeedback(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,


        @ManyToOne(fetch = FetchType.LAZY)
        val author: User,

        @Column(nullable = false)
        val isPositive: Boolean,

        @ManyToOne(fetch = FetchType.LAZY)
        val target: User,

        @DateTimeFormat
        @Column(nullable = false)
        val createdAt: LocalDateTime = LocalDateTime.now()
)