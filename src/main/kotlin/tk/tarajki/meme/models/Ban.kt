package tk.tarajki.meme.models

import java.time.LocalDateTime
import javax.persistence.*


@Entity
data class Ban(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @Column(nullable = false)
        val expireAt: LocalDateTime,

        @Column(nullable = false)
        val reason: String,

        @ManyToOne(fetch = FetchType.LAZY)
        val target: User,

        @ManyToOne(fetch = FetchType.LAZY)
        val invoker: User,

        @Column(nullable = false)
        val createdAt: LocalDateTime = LocalDateTime.now()
)