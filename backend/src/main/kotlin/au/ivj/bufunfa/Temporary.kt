package au.ivj.bufunfa

import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDate

@Configuration
class Temporary {
    @Bean
    fun loadDB(
        categoryRepository: CategoryRepository,
        accountRepository: AccountRepository,
        transactionRepository: TransactionRepository
    ): CommandLineRunner? {
        return CommandLineRunner { args: Array<String?>? ->
            listOf("Leisure", "Groceries", "Salary")
                .forEach { categoryRepository.save(Category(it)) }
            listOf("Cheque", "Cash")
                .forEach { accountRepository.save(Account(it, 10000)) }
            listOf(
                Transaction(
                    "Aldi",
                    accountRepository.findByName("Cheque")!!,
                    categoryRepository.findByName("Groceries")!!,
                    -2552,
                    LocalDate.now()
                ),
                Transaction(
                    "Coffee",
                    accountRepository.findByName("Cash")!!,
                    categoryRepository.findByName("Leisure")!!,
                    -540,
                    LocalDate.now().minusDays(1)
                ),
                Transaction(
                    "Coffee",
                    accountRepository.findByName("Cash")!!,
                    categoryRepository.findByName("Leisure")!!,
                    -540,
                    LocalDate.now().minusDays(2)
                ),
                Transaction(
                    "Coffee",
                    accountRepository.findByName("Cash")!!,
                    categoryRepository.findByName("Leisure")!!,
                    -540,
                    LocalDate.now().minusDays(3)
                ),
                Transaction(
                    "Salary",
                    accountRepository.findByName("Cheque")!!,
                    categoryRepository.findByName("Salary")!!,
                    150000,
                    LocalDate.now().withDayOfMonth(1)
                ),
                Transaction(
                    "Coffee",
                    accountRepository.findByName("Cash")!!,
                    categoryRepository.findByName("Leisure")!!,
                    -540,
                    LocalDate.now().withDayOfMonth(2).minusMonths(1)
                ),
                Transaction(
                    "Salary",
                    accountRepository.findByName("Cheque")!!,
                    categoryRepository.findByName("Salary")!!,
                    150000,
                    LocalDate.now().minusMonths(1).withDayOfMonth(1)
                )
            )
                .forEach { transactionRepository.save(it) }
        }
    }
}
