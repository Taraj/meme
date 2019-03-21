package tk.tarajki.meme.models

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.Future

@Entity
data class ActivationToken(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @field:Future
        @DateTimeFormat
        @Column(nullable = false)
        val expireAt: LocalDateTime,

        @Column(nullable = false)
        val context: String,

        @ManyToOne(fetch = FetchType.LAZY)
        val owner: User,

        @DateTimeFormat
        @Column(nullable = false)
        val createdAt: LocalDateTime = LocalDateTime.now()
)