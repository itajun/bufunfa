package au.ivj.bufunfa

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.web.cors.CorsConfiguration

@SpringBootApplication
@EnableCaching
@EnableTransactionManagement
@EnableScheduling
@EnableWebSecurity
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args) {
        setBannerMode(Banner.Mode.OFF)
    }
}

@Configuration
class WebSecurityConfig : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http
            .authorizeRequests()
            .antMatchers("/", "/manifest.json", "*.ico", "*.png", "/static/**").permitAll()
            .antMatchers("/graphql/**", "/subscriptions/**").hasRole("USER")
            .antMatchers("/graphiql/**").hasRole("ADMIN")
            .anyRequest().authenticated()
            .and()
            .httpBasic()
            .and()
            .cors().configurationSource {
                CorsConfiguration().applyPermitDefaultValues()
            }
            .and()
            .csrf().disable()
    }

    @Bean
    public override fun userDetailsService(): UserDetailsService {
        val admin: UserDetails = User.withDefaultPasswordEncoder()
            .username("admin")
            .password("admin")
            .roles("USER", "ADMIN")
            .build()
        val user: UserDetails = User.withDefaultPasswordEncoder()
            .username("user")
            .password("user")
            .roles("USER")
            .build()
        return InMemoryUserDetailsManager(admin, user)
    }
}