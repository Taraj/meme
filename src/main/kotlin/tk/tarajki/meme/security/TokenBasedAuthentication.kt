package tk.tarajki.meme.security

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails

class TokenBasedAuthentication(private val principle: UserDetails, private val token: String) : AbstractAuthenticationToken(principle.authorities) {

    override fun getCredentials(): Any {
        return token
    }

    override fun getPrincipal(): UserDetails {
        return principle
    }

    override fun isAuthenticated(): Boolean {
        return true
    }
}