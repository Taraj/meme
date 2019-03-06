package tk.tarajki.meme.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

@ResponseStatus(value = HttpStatus.CONFLICT)
class UserRegisterException(
        message: String
) : RuntimeException(message)