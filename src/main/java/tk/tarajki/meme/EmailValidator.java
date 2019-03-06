package tk.tarajki.meme;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<Email, String> {


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return VALID_EMAIL_ADDRESS_REGEX .matcher(value).find();

    }

    private final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    public void initialize(Email constraintAnnotation) {

    }
}
