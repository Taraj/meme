package tk.tarajki.meme.models

import javax.persistence.*


@Entity
data class Tag(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @Column(nullable = false, unique = true, length = 32)
        val name: String,

        @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
        val posts: List<Post>? = null

)