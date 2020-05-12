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
    val accountName: String,
    val amount: Long
)

class CreateAccountInput(
    val name: String,
    val initialAmount: Long
)

class CreateCategoryInput(
    val name: String
)

class CreateTransactionInput(
    val description: String,
    val accountId: Long,
    val categoryId: Long,
    val amount: Long,
    val date: LocalDate
)