package tk.tarajki.meme.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import tk.tarajki.meme.models.RoleName
import tk.tarajki.meme.models.User
import java.util.*
import javax.transaction.Transactional


class UserPrincipal(val user: User) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return arrayListOf(SimpleGrantedAuthority(user.role.name.name))
    }

    override fun isEnabled(): Boolean {
        return true
    }

    fun getRole(): RoleName {
        return user.role.name
    }

    override fun getUsername(): String {
        return user.username
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun getPassword(): String {
        return user.password
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {

        val isBaned = user.bans?.any {
            it.expireAt > Date()
        } ?: false

        return !isBaned
    }
}