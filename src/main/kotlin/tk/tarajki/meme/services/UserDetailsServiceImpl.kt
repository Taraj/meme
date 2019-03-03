package tk.tarajki.meme.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import tk.tarajki.meme.security.UserPrincipal
//import javax.transaction.Transactional

@Service
class UserDetailsServiceImpl : UserDetailsService {

    @Autowired
    private lateinit var userService: UserService


    override fun loadUserByUsername(username: String): UserDetails {
        val user = userService.findUserByUsername(username) ?: throw UsernameNotFoundException(username)

        return UserPrincipal(user)
    }
}