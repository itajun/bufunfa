package au.ivj.bufunfa.graphql

import au.ivj.bufunfa.*
import com.coxautodev.graphql.tools.GraphQLMutationResolver
import org.springframework.stereotype.Component

@Component
class BasicsMutationResolver(
    val accountRepository: AccountRepository,
    val categoryRepository: CategoryRepository
) : GraphQLMutationResolver {
    fun createAccount(input: CreateAccountInput) =
        accountRepository.save(Account(input.name, input.initialAmount))

    fun createCategory(input: CreateCategoryInput) =
        categoryRepository.save(Category(input.name))
}