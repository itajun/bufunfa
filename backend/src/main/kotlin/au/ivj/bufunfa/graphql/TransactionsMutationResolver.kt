package au.ivj.bufunfa.graphql

import au.ivj.bufunfa.*
import com.coxautodev.graphql.tools.GraphQLMutationResolver
import org.springframework.stereotype.Component

@Component
class TransactionsMutationResolver(
    val transactionRepository: TransactionRepository,
    val accountRepository: AccountRepository,
    val categoryRepository: CategoryRepository
) : GraphQLMutationResolver {
    fun createTransaction(input: CreateTransactionInput): Transaction {
        val category = categoryRepository.findById(input.categoryId)
            .orElseThrow({ IllegalArgumentException("Category not found") })
        val account = accountRepository.findById(input.accountId)
                .orElseThrow({ IllegalArgumentException("Account not found") })

        return transactionRepository.save(
            Transaction(
                input.description,
                account,
                category,
                input.amount,
                input.date
            )
        )
    }

    fun deleteTransaction(transactionId: Long): Boolean {
        transactionRepository.deleteById(transactionId)
        return true
    }
}