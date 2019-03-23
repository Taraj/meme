package tk.tarajki.meme.services

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import tk.tarajki.meme.models.User


@Service
class EmailService(
        private val javaMailSender: JavaMailSender
) {

    private fun sendEmail(email: String, subject: String, content: String) {
        val message = SimpleMailMessage()
        message.setTo(email)
        message.setSubject(subject)
        message.setText(content)
      //  javaMailSender.send(message)
    }

    fun sendConfirmationEmail(user: User, code: Int) {
        val message = "Twój kod aktywacyjny to: $code"

        sendEmail(user.email, "Aktywacja Konta", message)

    }

    fun sendResetPasswordRequest(user: User, code: Int) {
        val message = "Twoj kod resetujący hasło to: $code"

        sendEmail(user.email, "Reset Hasła", message)
    }

    fun sendNewPassword(user: User, password: String) {
        val message = "Twoje nowe hasło to: $password"

        sendEmail(user.email, "Nowe Hasło", message)
    }
}
