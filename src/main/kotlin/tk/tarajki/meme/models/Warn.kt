package tk.tarajki.meme.models

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
data class Warn(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @Column
        @DateTimeFormat
        val readAt: LocalDateTime? = null,

        @field:NotBlank
        @Column(nullable = false)
        val reason: String,

        @ManyToOne(fetch = FetchType.LAZY)
        val target: User,

        @ManyToOne(fetch = FetchType.LAZY)
        var invoker: User,

        @DateTimeFormat
        @Column(nullable = false)
        var createdAt: LocalDateTime = LocalDateTime.now()

)