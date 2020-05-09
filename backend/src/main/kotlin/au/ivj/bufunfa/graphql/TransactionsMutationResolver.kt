package au.ivj.bufunfa.graphql

import au.ivj.bufunfa.TransactionRepository
import com.coxautodev.graphql.tools.GraphQLMutationResolver
import org.springframework.stereotype.Component

@Component
class TransactionsMutationResolver(val transactionRepository: TransactionRepository) : GraphQLMutationResolver {
    fun deleteTransaction(transactionId: Long) =
        try {
            transactionRepository.deleteById(transactionId);
            true
        } catch (e: Exception) {
            false
        }
}