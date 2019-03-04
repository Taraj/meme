package tk.tarajki.meme.models

import org.springframework.format.annotation.DateTimeFormat
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Future
import javax.validation.constraints.NotBlank

@Entity
data class Ban(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @Future
        @DateTimeFormat
        @Column(nullable = false)
        val expireAt: Date,

        @NotBlank
        @Column(nullable = false)
        val reason: String,

        @ManyToOne(fetch = FetchType.LAZY)
        val target: User,

        @ManyToOne(fetch = FetchType.LAZY)
        var invoker: User,

        @DateTimeFormat
        @Column(nullable = false)
        var createdAt: Date = Date()
)