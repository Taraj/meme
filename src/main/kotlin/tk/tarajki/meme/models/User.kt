package tk.tarajki.meme.models

import javax.persistence.*

@Entity
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,

        @Column(nullable = false)
        val login: String,

        @Column(nullable = false)
        var nickname: String,

        @Column(nullable = false)
        var email: String,

        @Column(nullable = false)
        val password: String
)