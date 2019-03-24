package tk.tarajki.meme.models

import java.time.LocalDateTime
import javax.persistence.*


@Entity
data class Role(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @Column(nullable = false)
        val name: RoleName,

        @JoinColumn(name = "role_id")
        @OneToMany(fetch = FetchType.LAZY)
        val users: List<User>? = null,

        @Column(nullable = false)
        val createdAt: LocalDateTime = LocalDateTime.now()
)