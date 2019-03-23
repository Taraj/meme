package tk.tarajki.meme.validators.implementations

import tk.tarajki.meme.validators.annotations.TagList
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class TagListValidator : ConstraintValidator<TagList, List<String>> {
    override fun isValid(tagList: List<String>?, context: ConstraintValidatorContext?): Boolean {
        if (tagList == null) {
            return false
        }
        return tagList.all {
            it.trim().length in 2..10
        }
    }
}