package tk.tarajki.meme.validators.implementations

import tk.tarajki.meme.validators.annotations.PostTitle
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class PostTitleValidator : ConstraintValidator<PostTitle, String> {
    override fun isValid(title: String?, context: ConstraintValidatorContext?): Boolean {

        if (title == null) {
            return false
        }

        if (title.trim().length !in 3..32) {
            return false
        }



        return true
    }
}