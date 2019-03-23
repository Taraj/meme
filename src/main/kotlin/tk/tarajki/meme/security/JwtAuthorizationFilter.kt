package tk.tarajki.meme.security

import com.auth0.jwt.exceptions.TokenExpiredException
import org.springframework.context.annotation.Lazy
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import tk.tarajki.meme.exceptions.UserAuthException
import tk.tarajki.meme.services.UserDetailsServiceImpl
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthorizationFilter(
        private val jwtTokenProvider: JwtTokenProvider,
        @Lazy private val userDetailsService: UserDetailsServiceImpl
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {

        val token = getTokenFromRequest(request)

        if (token != null) {
            val tokenPayload = try {
                jwtTokenProvider.getTokenPayload(token)
            } catch (e: TokenExpiredException) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired.")
                return
            } catch (e: Exception) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Something is wrong with Token: $e.")
                return
            }

            val username = jwtTokenProvider.getUsernameFromTokenPayload(tokenPayload)
            val lastTokenRelease = try {
                jwtTokenProvider.getLastTokenReleaseFromTokenPayload(tokenPayload)
            } catch (e: UserAuthException) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Something is wrong with Token: ${e.message}.")
                return
            }


            val userDetails = try {
                userDetailsService.loadUserByUsername(username)
            } catch (e: UsernameNotFoundException) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not found.")
                return
            } as UserPrincipal

            if (!userDetails.isAccountNonLocked) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Your account is baned.")
                return
            }

            if (lastTokenRelease != userDetails.user.lastTokenRelease) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token outdated.")
            }

            val authentication = TokenBasedAuthentication(userDetails, token)
            SecurityContextHolder.getContext().authentication = authentication

        }

        chain.doFilter(request, response)
    }


    private fun getTokenFromRequest(request: HttpServletRequest): String? {
        val header = request.getHeader("Authorization") ?: return null

        return if (!header.startsWith("Bearer ")) {
            null
        } else {
            return header.removePrefix("Bearer ")
        }
    }


}