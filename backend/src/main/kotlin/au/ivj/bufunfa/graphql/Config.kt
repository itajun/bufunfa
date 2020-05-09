package au.ivj.bufunfa.graphql

import graphql.scalars.ExtendedScalars
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Config {
    @Bean
    fun dateScalar() = ExtendedScalars.Date
}