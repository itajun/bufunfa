package au.ivj.bufunfa

import org.apache.catalina.connector.Connector
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer
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
import org.springframework.stereotype.Component
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
            .antMatchers("/", "/static/**").permitAll()
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

/**
 * Here only to enable 2 connectors HTTP/HTTPS, since Spring Boot adds only one by default.
 */
@Component
class TomcatConfig : WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
    @Value("\${server.http.port}")
    private val httpPort = 0

    override fun customize(factory: TomcatServletWebServerFactory) {
        val connector = Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL)
        connector.port = httpPort
        factory.addAdditionalTomcatConnectors(connector)
    }
}