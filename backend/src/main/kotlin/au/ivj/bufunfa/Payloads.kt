package au.ivj.bufunfa

import java.time.LocalDate
import java.time.LocalDateTime

class GetStatementInput(
    val from: LocalDate,
    val to: LocalDate,
    val categories: Set<Long>?,
    val accounts: Set<Long>?
)

class Statement(
    val transactions: List<Transaction>,
    val initialAccountTotals: List<AccountTotal>,
    val date: LocalDateTime
)

class AccountTotal(
    val accountId: Long,
    val amount: Long
)