package tk.tarajki.meme.models

import java.time.LocalDateTime
import java.util.concurrent.ThreadLocalRandom
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @field:NotBlank
        @Size(min = 3, max = 32)
        @Column(nullable = false, unique = true, length = 32)
        val username: String,

        @field:NotBlank
        @Column(nullable = false)
        val avatarURL: String = "https://www.w3schools.com/w3css/img_avatar3.png",

        @field:NotBlank
        @Size(min = 3, max = 32)
        @Column(nullable = false, unique = true, length = 32)
        val nickname: String,

        @field:Email
        @field:NotBlank
        @Size(min = 3, max = 64)
        @Column(nullable = false, unique = true, length = 64)
        val email: String,

        @field:NotBlank
        @Column(nullable = false)
        val password: String,

        @ManyToOne(fetch = FetchType.EAGER)
        val role: Role,

        @JoinColumn(name = "target_id")
        @OneToMany(fetch = FetchType.EAGER)
        val bans: List<Ban>? = null,

        @JoinColumn(name = "target_id")
        @OneToMany(fetch = FetchType.LAZY)
        val warns: List<Warn>? = null,

        @JoinColumn(name = "author_id")
        @OneToMany(fetch = FetchType.LAZY)
        val posts: List<Post>? = null,

        @JoinColumn(name = "author_id")
        @OneToMany(fetch = FetchType.LAZY)
        val comments: List<Comment>? = null,

        @Column
        val activationToken: Int? = ThreadLocalRandom.current().nextInt(1000, 9999),

        @Column(nullable = false)
        val lastUpdate: LocalDateTime = LocalDateTime.now(),

        @Column(nullable = false)
        val createdAt: LocalDateTime = LocalDateTime.now()
)