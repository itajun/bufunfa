package au.ivj.bufunfa.graphql

import au.ivj.bufunfa.TransactionRepository
import com.coxautodev.graphql.tools.GraphQLQueryResolver
import org.springframework.stereotype.Component

@Component
class TransactionsQueryResolver(val transactionRepository: TransactionRepository) : GraphQLQueryResolver {
    fun transactions() = transactionRepository.findAll().toList()
}