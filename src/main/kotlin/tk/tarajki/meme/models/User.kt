package tk.tarajki.meme.models

import javax.persistence.*

@Entity
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @Column(nullable = false, unique = true, length = 32)
        val login: String,

        @Column(nullable = false, unique = true, length = 32)
        var nickname: String,

        @Column(nullable = false, unique = true, length = 32)
        var email: String,

        @Column(nullable = false)
        val password: String
)