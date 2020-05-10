package au.ivj.bufunfa.graphql

import com.coxautodev.graphql.tools.ObjectMapperConfigurer
import com.coxautodev.graphql.tools.ObjectMapperConfigurerContext
import com.coxautodev.graphql.tools.SchemaParserOptions
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import graphql.scalars.ExtendedScalars
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class Config {
    @Bean
    fun dateScalar() = ExtendedScalars.Date

    // See: https://github.com/graphql-java-kickstart/graphql-spring-boot/issues/32
    @Bean
    fun schemaParserOptions(): SchemaParserOptions {
        return SchemaParserOptions.newOptions()
            .objectMapperConfigurer(ObjectMapperConfigurer { mapper, _ ->
                mapper.registerModule(
                    JavaTimeModule()
                )
            })
            .build()
    }
}