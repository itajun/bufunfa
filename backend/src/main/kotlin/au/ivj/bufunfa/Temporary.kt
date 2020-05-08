package au.ivj.bufunfa

import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Temporary {
    @Bean
    fun loadDB(categoryRepository: CategoryRepository): CommandLineRunner? {
        return CommandLineRunner { args: Array<String?>? ->
            listOf("First", "Second", "Third", "Aaaaaaaaaaaaaaaaad the long one")
                .forEach { categoryRepository.save(Category(it)) }
        }
    }
}
