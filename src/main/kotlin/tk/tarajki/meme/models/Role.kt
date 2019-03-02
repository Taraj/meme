package tk.tarajki.meme.models


import javax.persistence.*

@Entity
data class Role(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,


        @Enumerated(EnumType.STRING)
        @Column(length = 60)
        val name: RoleName,


        @JoinColumn
        @OneToMany(fetch = FetchType.LAZY)
        val users: List<User>?

)