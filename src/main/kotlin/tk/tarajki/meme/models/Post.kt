package tk.tarajki.meme.models


import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
data class Post(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @field:NotBlank
        @Column(nullable = false)
        val title: String,

        @field:NotBlank
        @Column(nullable = false)
        val url: String,

        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "post_tags",
                joinColumns = [JoinColumn(name = "post_id")],
                inverseJoinColumns = [JoinColumn(name = "tag_id")])
        val tags: List<Tag>,

        @JoinColumn
        @ManyToOne(fetch = FetchType.LAZY)
        val author: User,

        @JoinColumn
        @ManyToOne(fetch = FetchType.LAZY)
        val confirmedBy: User? = null,

        @Column
        val confirmedAt: LocalDateTime? = null,

        @JoinColumn
        @ManyToOne(fetch = FetchType.LAZY)
        val deletedBy: User? = null,

        @Column
        val deletedAt: LocalDateTime? = null,

        @JoinColumn(name = "post_id")
        @OneToMany(fetch = FetchType.LAZY)
        val comments: List<PostComment>? = null,

        @JoinColumn(name = "target_id")
        @OneToMany(fetch = FetchType.LAZY)
        val postFeedback: List<PostFeedback>? = null,

        @Column(nullable = false)
        val createdAt: LocalDateTime = LocalDateTime.now()
)
