package tk.tarajki.meme.validators.implementations

import tk.tarajki.meme.validators.annotations.Comment

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class CommentValidator : ConstraintValidator<Comment, String> {
    override fun isValid(comment: String?, context: ConstraintValidatorContext?): Boolean {

        if (comment == null) {
            return false
        }

        if (comment.length > 2048) {
            return false
        }
        return true
    }
}