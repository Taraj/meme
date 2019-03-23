package tk.tarajki.meme.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import tk.tarajki.meme.exceptions.UserAuthException
import java.time.LocalDateTime
import java.time.ZoneOffset

@Component
class JwtTokenProvider {

    @Value("\${security.jwt.token.secret-key}")
    private lateinit var secretKey: String


    fun createToken(username: String, lastTokenRelease: LocalDateTime): String {
        return JWT.create()
                .withSubject(username)
                .withClaim("lastTokenRelease", lastTokenRelease.toEpochSecond(ZoneOffset.UTC))
                .sign(Algorithm.HMAC512(secretKey))
    }

    fun getTokenPayload(token: String): DecodedJWT {
        return JWT.require(Algorithm.HMAC512(secretKey))
                .build()
                .verify(token)
    }

    fun getUsernameFromTokenPayload(payload: DecodedJWT): String {
        return payload.subject
    }

    fun getLastTokenReleaseFromTokenPayload(payload: DecodedJWT): LocalDateTime {
        val date = payload.claims["lastTokenRelease"] ?: throw UserAuthException("Bad Token")

        return LocalDateTime.ofEpochSecond(date.asLong(), 0, ZoneOffset.UTC)
    }
}
