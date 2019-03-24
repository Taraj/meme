package tk.tarajki.meme.validators.annotations

import tk.tarajki.meme.validators.implementations.UsernameValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [UsernameValidator::class])
annotation class Username(
        val message: String = "Username is not valid",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = []
)
