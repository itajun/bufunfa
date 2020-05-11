package au.ivj.bufunfa.graphql

import au.ivj.bufunfa.AccountRepository
import au.ivj.bufunfa.CategoryRepository
import com.coxautodev.graphql.tools.GraphQLQueryResolver
import graphql.GraphQLError
import graphql.servlet.GenericGraphQLError
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ExceptionHandler;

@Component
@Configuration
class BasicsQueryResolver(
    val categoryRepository: CategoryRepository,
    val accountRepository: AccountRepository
) : GraphQLQueryResolver {
    fun categories() = categoryRepository.findAll().toList()
    fun accounts() = accountRepository.findAll().toList()
}