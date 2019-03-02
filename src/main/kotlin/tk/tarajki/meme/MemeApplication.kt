package tk.tarajki.meme

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import tk.tarajki.meme.services.UserDetailsServiceImpl

@SpringBootApplication
class MemeApplication {


    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun userDetailsService(): UserDetailsService {
        return UserDetailsServiceImpl()
    }

}

fun main(args: Array<String>) {
    runApplication<MemeApplication>(*args)
}
