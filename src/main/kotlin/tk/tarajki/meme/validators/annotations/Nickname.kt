package tk.tarajki.meme.validators.annotations

import tk.tarajki.meme.validators.implementations.NicknameValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [NicknameValidator::class])
annotation class Nickname(
        val message: String = "Nickname is not valid",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = []
)
