package tk.tarajki.meme.models

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
        @Column(nullable = false, unique = true, length = 32)
        var nickname: String,


        @Email
        @NotBlank
        @Size(min = 3, max = 32)
        @Column(nullable = false, unique = true, length = 32)
        var email: String,


        @NotBlank
        @Column(nullable = false)
        val password: String,


        @ManyToOne(fetch = FetchType.LAZY)
        val role: Role,


        @JoinColumn
        @OneToMany(fetch = FetchType.LAZY)
        var bans: List<Ban>? = null
)