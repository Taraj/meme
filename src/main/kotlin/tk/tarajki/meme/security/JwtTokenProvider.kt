package tk.tarajki.meme.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class JwtTokenProvider {

    @Value("\${security.jwt.token.secret-key}")
    private lateinit var secretKey: String



    fun createToken(username: String): String {
        return JWT.create()
                .withSubject(username)
                .sign(Algorithm.HMAC512(secretKey))
    }

    fun getUsernameFromToken(token: String): String {
        return JWT.require(Algorithm.HMAC512(secretKey))
                .build()
                .verify(token)
                .subject
    }

}