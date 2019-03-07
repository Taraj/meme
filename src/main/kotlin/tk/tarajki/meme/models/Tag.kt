package tk.tarajki.meme.models

import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
data class Tag(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @field:NotBlank
        @field:Size(min = 3, max = 32)
        @Column(nullable = false, unique = true, length = 32)
        val name: String,

        @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
        val posts: List<Post>? = null

)