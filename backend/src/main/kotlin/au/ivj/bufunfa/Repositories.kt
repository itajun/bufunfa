package au.ivj.bufunfa

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.time.LocalDate

interface CategoryRepository : CrudRepository<Category, Long> {
    fun findByName(name: String): Category?
}

interface AccountRepository : CrudRepository<Account, Long> {
    fun findByName(name: String): Account?

    @Query(
        "SELECT " +
                "    new au.ivj.bufunfa.AccountTotal(t.account.id, coalesce(SUM(t.amount), 0)) " +
                "FROM " +
                "    Transaction t " +
                "WHERE " +
                "    t.date < :date " +
                "    AND (:accounts IS NULL OR t.account.id IN :accounts) " +
                "GROUP BY " +
                "    t.account"
    )
    fun findAccountTotalsBefore(date: LocalDate, accounts: Set<Long>?): List<AccountTotal> // varargs doesn't seem to work
}

interface TransactionRepository : CrudRepository<Transaction, Long> {
    @Query("select t from Transaction t where t.date between :from and :to")
    fun findByDateIsBetween(from: LocalDate?, to: LocalDate?): List<Transaction>
}