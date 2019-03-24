package tk.tarajki.meme.validators.annotations

import tk.tarajki.meme.validators.implementations.PostTitleValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass


@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [PostTitleValidator::class])
annotation class PostTitle(
        val message: String = "Post title is not valid",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = []
)
