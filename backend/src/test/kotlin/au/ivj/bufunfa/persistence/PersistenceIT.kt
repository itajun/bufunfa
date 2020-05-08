package au.ivj.bufunfa.persistence

import au.ivj.bufunfa.AccountRepository
import au.ivj.bufunfa.Category
import au.ivj.bufunfa.CategoryRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DataJpaTest
class RepositoriesTests @Autowired constructor(
    val entityManager: TestEntityManager,
    val categoryRepository: CategoryRepository,
    val accountRepository: AccountRepository) {

    @Test
    fun `When a category is created, I can find it by name`() {
        val xpto = Category("xpto")
        entityManager.persist(xpto)
        entityManager.flush()
        val findByName = categoryRepository.findByName("xpto")

        assertThat(findByName).isEqualTo(xpto)
    }
}