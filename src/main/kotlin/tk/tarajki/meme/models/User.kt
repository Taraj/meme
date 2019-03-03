package tk.tarajki.meme.models

import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,


        @NotBlank
        @Size(min = 3, max = 32)
        @Column(nullable = false, unique = true, length = 32)
        val username: String,


        @NotBlank
        @Size(min = 3, max = 32)
        @Column(nullable = false, length = 32)
        val avatarURL: String = "http://url.xy",


        @NotBlank
        @Size(min = 3, max = 32)
        @Column(nullable = false, unique = true, length = 32)
        var nickname: String,

        @Email
        @NotBlank
        @Size(min = 3, max = 64)
        @Column(nullable = false, unique = true, length = 64)
        var email: String,

        @NotBlank
        @Column(nullable = false)
        val password: String,

        @ManyToOne(fetch = FetchType.EAGER)
        val role: Role,

        @JoinColumn(name = "target_id")
        @OneToMany(fetch = FetchType.EAGER)

        var bans: List<Ban>? = null,

        @JoinColumn(name = "target_id")
        @OneToMany(fetch = FetchType.LAZY)
        var warns: List<Warn>? = null,

        @JoinColumn(name = "id")
        @OneToMany(fetch = FetchType.LAZY)
        var posts: List<Post>? = null,


        @Column(nullable = false)
        var createdAt: Date = Date()
)