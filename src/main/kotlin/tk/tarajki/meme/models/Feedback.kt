package tk.tarajki.meme.models

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.Future
import javax.validation.constraints.NotBlank

@Entity
data class Feedback(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @field:NotBlank
        @Column(nullable = false)
        val authorIp: String,

        @Column(nullable = false)
        val isPositive: Boolean,

        @JoinColumn
        @ManyToOne(fetch = FetchType.LAZY)
        val post: Post,

        @DateTimeFormat
        @Column(nullable = false)
        val createdAt: LocalDateTime = LocalDateTime.now()
)