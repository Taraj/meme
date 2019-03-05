package tk.tarajki.meme.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import tk.tarajki.meme.security.JwtAuthorizationFilter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import tk.tarajki.meme.models.RoleName
import tk.tarajki.meme.services.UserDetailsServiceImpl


@Configuration
@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var userDetailsService: UserDetailsServiceImpl

    @Autowired
    private lateinit var bCryptPasswordEncoder: BCryptPasswordEncoder

    @Autowired
    private lateinit var jwtAuthorizationFilter: JwtAuthorizationFilter


    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder)
    }

    @Bean
    override fun authenticationManager(): AuthenticationManager {
        return super.authenticationManager()
    }

    override fun configure(http: HttpSecurity) {
        http.csrf().disable()

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        http.authorizeRequests()
                .antMatchers("/api/v1/auth/*").permitAll()
                .antMatchers("/error").permitAll()
                .antMatchers("/api/v1/users/*").permitAll()
                .antMatchers("/api/v1/users/*/ban").hasAuthority(RoleName.ROLE_ADMIN.name)
                .antMatchers("/api/v1/users/*/warn").hasAuthority(RoleName.ROLE_ADMIN.name)
                .antMatchers("/api/v1/users/*/posts").permitAll()
                .antMatchers("/api/v1/users/*/comments").permitAll()

                .anyRequest().authenticated()



        http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter::class.java)

    }


}