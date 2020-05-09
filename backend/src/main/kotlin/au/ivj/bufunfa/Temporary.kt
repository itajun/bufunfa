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
                .forEach { accountRepository.save(Account(it)) }
            listOf(
                Transaction(
                    "First transaction",
                    accountRepository.findByName("Acc 1")!!,
                    categoryRepository.findByName("Cat 1")!!,
                    10,
                    null,
                    LocalDate.now(),
                    null
                ),
                Transaction(
                    "Second transaction",
                    accountRepository.findByName("Acc 2")!!,
                    categoryRepository.findByName("Cat 2")!!,
                    10,
                    null,
                    LocalDate.now(),
                    null
                ),
                Transaction(
                    "Third transaction",
                    accountRepository.findByName("Acc 1")!!,
                    categoryRepository.findByName("Cat 2")!!,
                    10,
                    null,
                    LocalDate.now(),
                    null
                )
            )
                .forEach { transactionRepository.save(it) }
        }
    }
}
