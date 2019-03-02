package tk.tarajki.meme.models

import org.springframework.format.annotation.DateTimeFormat
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Future

@Entity
data class Ban(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,

        @Future
        @DateTimeFormat
        @Column(nullable = false)
        val expires: Date,

        @ManyToOne
        val target: User

)