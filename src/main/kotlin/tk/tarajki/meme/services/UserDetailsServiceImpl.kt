package tk.tarajki.meme.services

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import tk.tarajki.meme.security.UserPrincipal


@Service
class UserDetailsServiceImpl(
        val userService: UserService
) : UserDetailsService {


    override fun loadUserByUsername(username: String): UserDetails {
        val user = userService.findUserByUsername(username) ?: throw UsernameNotFoundException(username)

        return UserPrincipal(user)
    }
}