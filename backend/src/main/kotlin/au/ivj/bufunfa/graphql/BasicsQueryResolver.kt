package au.ivj.bufunfa.graphql

import au.ivj.bufunfa.AccountRepository
import au.ivj.bufunfa.CategoryRepository
import com.coxautodev.graphql.tools.GraphQLQueryResolver
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
@Configuration
class BasicsQueryResolver(
    val categoryRepository: CategoryRepository,
    val accountRepository: AccountRepository
) : GraphQLQueryResolver {
    fun categories() = categoryRepository.findAll().toList()
    fun accounts() = accountRepository.findAll().toList()
    fun me() = SecurityContextHolder.getContext().authentication.name
}