package tk.tarajki.meme.validators.annotations

import tk.tarajki.meme.validators.implementations.CommentValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [CommentValidator::class])
annotation class Comment(
        val message: String = "Comment is not valid",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = []
)
