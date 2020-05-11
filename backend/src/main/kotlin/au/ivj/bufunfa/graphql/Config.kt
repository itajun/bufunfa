package au.ivj.bufunfa.graphql

import com.coxautodev.graphql.tools.ObjectMapperConfigurer
import com.coxautodev.graphql.tools.SchemaParserOptions
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import graphql.ExceptionWhileDataFetching
import graphql.GraphQLError
import graphql.scalars.ExtendedScalars
import graphql.servlet.GenericGraphQLError
import graphql.servlet.GraphQLErrorHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.dao.DataIntegrityViolationException
import java.util.*
import java.util.stream.Collectors


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

    @Bean
    fun errorHandler(): GraphQLErrorHandler? {
        return object : GraphQLErrorHandler {
            override fun processErrors(errors: List<GraphQLError>): List<GraphQLError> {
                val clientErrors = errors.stream()
                    .filter { error: GraphQLError? ->
                        isClientError(
                            error
                        )
                    }
                    .collect(Collectors.toList())
                val serverErrors: List<GraphQLError> = errors.stream()
                    .filter { e: GraphQLError? -> !isClientError(e) }
                    .map(this::normaliseError)
                    .collect(Collectors.toList<GraphQLError>())
                val e: MutableList<GraphQLError> = ArrayList()
                e.addAll(clientErrors)
                e.addAll(serverErrors)
                return e
            }

            private fun isClientError(error: GraphQLError?): Boolean {
                return !(error is ExceptionWhileDataFetching || error is Throwable)
            }

            private fun normaliseError(error: GraphQLError): GraphQLError {
                if (error is ExceptionWhileDataFetching) {
                    val exception = error.exception
                    if (exception is DataIntegrityViolationException) {
                        val message = exception.message ?: "unknown index"
                        val fieldNames = """\(([\w, ]+)\) VALUES""".toRegex()
                            .find(message)
                            ?.groupValues
                            ?.get(1) ?: "unknown index"
                        return GenericGraphQLError("Record with the same value exists for field(s) [$fieldNames]") // TODO maybe something with ID for internationalisation
                    }
                }

                return error;
            }
        }
    }
}