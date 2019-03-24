package tk.tarajki.meme.validators.implementations


import tk.tarajki.meme.validators.annotations.Username
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class UsernameValidator : ConstraintValidator<Username, String> {
    override fun isValid(username: String?, context: ConstraintValidatorContext?): Boolean {

        if (username == null) {
            return false
        }

        if (username.trim().length !in 3..32) {
            return false
        }

        val containsSpecialChar = username.any {
            !it.isLetterOrDigit()
        }

        if (containsSpecialChar) {
            return false
        }

        return true
    }
}