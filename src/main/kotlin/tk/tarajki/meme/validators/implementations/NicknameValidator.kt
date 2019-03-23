package tk.tarajki.meme.validators.implementations

import tk.tarajki.meme.validators.annotations.Comment
import tk.tarajki.meme.validators.annotations.Nickname
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class NicknameValidator : ConstraintValidator<Nickname, String> {
    override fun isValid(nickname: String?, context: ConstraintValidatorContext?): Boolean {

        if (nickname == null) {
            return false
        }

        if (nickname.trim().length !in 3..32) {
            return false
        }
        return true
    }
}