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
            listOf("Cat 1", "Cat 2", "Cat 3")
                .forEach { categoryRepository.save(Category(it)) }
            listOf("Acc 1", "Acc 2", "Acc 3")
                .forEach { accountRepository.save(Account(it, 1000)) }
            listOf(
                Transaction(
                    "First transaction",
                    accountRepository.findByName("Acc 1")!!,
                    categoryRepository.findByName("Cat 1")!!,
                    1000,
                    LocalDate.now()
                ),
                Transaction(
                    "Second transaction",
                    accountRepository.findByName("Acc 2")!!,
                    categoryRepository.findByName("Cat 2")!!,
                    1000,
                    LocalDate.now().minusMonths(3)
                ),
                Transaction(
                    "Third transaction",
                    accountRepository.findByName("Acc 1")!!,
                    categoryRepository.findByName("Cat 2")!!,
                    1000,
                    LocalDate.now().minusMonths(1)
                )
            )
                .forEach { transactionRepository.save(it) }
        }
    }
}
