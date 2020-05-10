package au.ivj.bufunfa.service

import au.ivj.bufunfa.*
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.stream.Collectors

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
                .sorted(
                    nullsFirst(compareBy(Transaction::confirmedDate, Transaction::date))
                )
                .collect(Collectors.toList<Transaction>())

        val accountTotals = transactions
            .stream()
            .map(Transaction::account)
            .distinct()
            .map {
                val sum = accountRepository.findConfirmedAccountTotalsBefore(getStatementInput.from, setOf(it.id!!)).firstOrNull()
                AccountTotal(it.id!!, it.initialAmount + (sum?.amount ?: 0))
            }
            .collect(Collectors.toList<AccountTotal>())

        return Statement(
            transactions,
            accountTotals,
            LocalDateTime.now()
        )
    }
}