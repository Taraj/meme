package tk.tarajki.meme.validators.annotations

import tk.tarajki.meme.validators.implementations.TagListValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [TagListValidator::class])
annotation class TagList(
        val message: String = "Tag list is not valid",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = []
)
