package au.ivj.bufunfa

import org.springframework.data.repository.CrudRepository

interface CategoryRepository : CrudRepository<Category, Long> {
    fun findByName(name: String): Category?
}

interface AccountRepository : CrudRepository<Account, Long> {
    fun findByName(name: String): Account?
}