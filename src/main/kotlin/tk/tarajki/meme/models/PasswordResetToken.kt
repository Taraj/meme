package tk.tarajki.meme.models

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.Future

@Entity
data class PasswordResetToken(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @field:Future
        @DateTimeFormat
        @Column(nullable = false)
        val expireAt: LocalDateTime,

        @Column(nullable = false)
        val code: Int,

        @ManyToOne(fetch = FetchType.LAZY)
        val target: User,

        @DateTimeFormat
        @Column(nullable = false)
        val createdAt: LocalDateTime = LocalDateTime.now()
)