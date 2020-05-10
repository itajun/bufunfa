package au.ivj.bufunfa.graphql

import au.ivj.bufunfa.AccountRepository
import au.ivj.bufunfa.CategoryRepository
import com.coxautodev.graphql.tools.GraphQLQueryResolver
import org.springframework.stereotype.Component

@Component
class BasicsQueryResolver(
    val categoryRepository: CategoryRepository,
    val accountRepository: AccountRepository
) : GraphQLQueryResolver {
    fun categories() = categoryRepository.findAll().toList()
    fun accounts() = accountRepository.findAll().toList()
}