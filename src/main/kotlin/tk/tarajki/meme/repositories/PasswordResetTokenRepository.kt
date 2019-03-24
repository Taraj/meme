package tk.tarajki.meme.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import tk.tarajki.meme.models.PasswordResetToken
import tk.tarajki.meme.models.User


@Repository
interface PasswordResetTokenRepository : JpaRepository<PasswordResetToken, Long> {
    fun findPasswordResetTokenByCode(code: Int): PasswordResetToken?

    fun findPasswordResetTokensByTarget(user: User): List<PasswordResetToken>?
}