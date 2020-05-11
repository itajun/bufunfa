package au.ivj.bufunfa.service

import au.ivj.bufunfa.*
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import kotlin.streams.toList

@Service
class StatementService(
    val accountRepository: AccountRepository,
    val transactionRepository: TransactionRepository
) {
    fun getStatement(getStatementInput: GetStatementInput): Statement {
        val transactions =
            transactionRepository.findByDateIsBetween(getStatementInput.from, getStatementInput.to)
                .stream()
                .filter { getStatementInput.accounts?.contains(it.account.id) ?: true }
                .filter { getStatementInput.categories?.contains(it.category.id) ?: true }
                .sorted(compareBy(Transaction::date))
                .toList()

        val accountTotals = accountRepository
            .findAll()
            .filter { getStatementInput.accounts?.contains(it.id) ?: true }
            .map {
                val sum =
                    accountRepository.findAccountTotalsBefore(getStatementInput.from, setOf(it.id!!)).firstOrNull()
                AccountTotal(it.id!!, it.initialAmount + (sum?.amount ?: 0))
            }
            .toList()

        return Statement(
            transactions,
            accountTotals,
            LocalDateTime.now()
        )
    }
}