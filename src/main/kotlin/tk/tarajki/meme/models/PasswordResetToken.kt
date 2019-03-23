package tk.tarajki.meme.models

import java.time.LocalDateTime
import javax.persistence.*


@Entity
data class PasswordResetToken(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @Column(nullable = false)
        val expireAt: LocalDateTime,

        @Column(nullable = false, unique = true)
        val code: Int,

        @ManyToOne(fetch = FetchType.LAZY)
        val target: User,

        @Column(nullable = false)
        val createdAt: LocalDateTime = LocalDateTime.now()
)