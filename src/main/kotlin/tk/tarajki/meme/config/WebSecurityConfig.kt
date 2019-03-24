package tk.tarajki.meme.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
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
class WebSecurityConfig(
        @Lazy private val userDetailsService: UserDetailsServiceImpl,
        @Lazy private val bCryptPasswordEncoder: BCryptPasswordEncoder,
        private val jwtAuthorizationFilter: JwtAuthorizationFilter
) : WebSecurityConfigurerAdapter() {


    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder)
    }

    @Bean
    override fun authenticationManager(): AuthenticationManager {
        return super.authenticationManager()
    }

    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    override fun configure(http: HttpSecurity) {
        http.csrf().disable()

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        http.authorizeRequests()
                .antMatchers("/error").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                .antMatchers("/api/v1/auth/**").permitAll()

                .antMatchers(HttpMethod.GET,"/api/v1/users").permitAll()
                .antMatchers(HttpMethod.GET,"/api/v1/users/*").permitAll()
                .antMatchers(HttpMethod.GET,"/api/v1/users/*/posts").permitAll()
                .antMatchers(HttpMethod.GET,"/api/v1/users/*/comments").permitAll()

                .antMatchers(HttpMethod.GET, "/api/v1/posts").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/posts/*").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/posts/*/comments").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/posts/*/feedback").permitAll()

                .antMatchers(HttpMethod.GET, "/api/v1/comments/*").permitAll()

                .antMatchers(HttpMethod.GET, "/api/v1/tags").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/tags/*/posts").permitAll()


                .antMatchers(HttpMethod.DELETE, "/api/v1/posts/*").hasAuthority(RoleName.ROLE_ADMIN.name)
                .antMatchers(HttpMethod.PUT, "/api/v1/posts/*").hasAuthority(RoleName.ROLE_ADMIN.name)


                .antMatchers(HttpMethod.GET, "/api/v1/posts/*/feedback").hasAuthority(RoleName.ROLE_ADMIN.name)
                .antMatchers(HttpMethod.GET, "/api/v1/comments/*/feedback").hasAuthority(RoleName.ROLE_ADMIN.name)
                .antMatchers(HttpMethod.GET, "/api/v1/users/*/feedback").hasAuthority(RoleName.ROLE_ADMIN.name)

                .antMatchers("/api/v1/users/*/bans").hasAuthority(RoleName.ROLE_ADMIN.name)
                .antMatchers("/api/v1/users/*/warns").hasAuthority(RoleName.ROLE_ADMIN.name)


                .anyRequest().authenticated()

        http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter::class.java)

    }

}