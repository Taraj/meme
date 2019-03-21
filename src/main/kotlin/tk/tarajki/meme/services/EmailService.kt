package tk.tarajki.meme.services

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service


@Service
class EmailService(
        private val javaMailSender: JavaMailSender
) {

    fun sendEmail(email: String, subject: String, content: String) {
        val message = SimpleMailMessage()
        message.setTo(email)
        message.setSubject(subject)
        message.setText(content)
        javaMailSender.send(message)
    }
}
