package tk.tarajki.meme.repositories

import org.springframework.data.jpa.repository.JpaRepository
import tk.tarajki.meme.models.PasswordResetToken

interface PasswordResetTokenRepository : JpaRepository<PasswordResetToken, Long> {
    fun findPasswordResetTokenByCode(code: Int): PasswordResetToken?
}