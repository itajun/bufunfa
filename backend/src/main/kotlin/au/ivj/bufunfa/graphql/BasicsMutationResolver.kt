package au.ivj.bufunfa.graphql

import au.ivj.bufunfa.Account
import au.ivj.bufunfa.AccountRepository
import au.ivj.bufunfa.CreateAccountInput
import com.coxautodev.graphql.tools.GraphQLMutationResolver
import org.springframework.stereotype.Component

@Component
class BasicsMutationResolver(val accountRepository: AccountRepository) : GraphQLMutationResolver {
    fun createAccount(input: CreateAccountInput) =
        accountRepository.save(Account(input.name, input.initialAmount))
}