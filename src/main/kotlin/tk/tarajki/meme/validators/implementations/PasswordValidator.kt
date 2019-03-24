package tk.tarajki.meme.validators.implementations

import tk.tarajki.meme.validators.annotations.Password
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class PasswordValidator : ConstraintValidator<Password, String> {
    override fun isValid(password: String?, context: ConstraintValidatorContext?): Boolean {

        if (password == null) {
            return false
        }

        if (password.trim().length !in 3..32) {
            return false
        }


        return true
    }
}