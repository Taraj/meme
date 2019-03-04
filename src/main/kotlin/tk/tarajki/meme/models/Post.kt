package tk.tarajki.meme.models


import java.util.*
import javax.persistence.*

@Entity
data class Post(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @Column(nullable = false)
        val title: String,

        @Column(nullable = false)
        val url: String,

        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "post_tags",
                joinColumns = [JoinColumn(name = "post_id")],
                inverseJoinColumns = [JoinColumn(name = "tag_id")])
        val tags: List<Tag>,

        @JoinColumn
        @ManyToOne(fetch = FetchType.LAZY)
        val creator: User,

        @Column(nullable = false)
        val createdAt: Date = Date()
)