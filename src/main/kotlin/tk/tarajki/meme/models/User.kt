package tk.tarajki.meme.models

import java.time.LocalDateTime
import java.util.concurrent.ThreadLocalRandom
import javax.persistence.*


@Entity
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @Column(nullable = false, unique = true, length = 32)
        val username: String,

        @Lob
        @Column(nullable = false)
        val avatarURL: String = "https://www.w3schools.com/w3css/img_avatar3.png",

        @Column(nullable = false, unique = true, length = 32)
        val nickname: String,

        @Column(nullable = false, unique = true, length = 64)
        val email: String,

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
        val activationToken: Int? = ThreadLocalRandom.current().nextInt(10000, 99999),

        @Column(nullable = false)
        val lastTokenRelease: LocalDateTime = LocalDateTime.now(),

        @Column(nullable = false)
        val createdAt: LocalDateTime = LocalDateTime.now()
)