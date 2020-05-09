package au.ivj.bufunfa

import java.time.LocalDate
import javax.persistence.*

@Entity
class Category(
    val name: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
)

@Entity
class Account(
    val name: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
)

@Entity
class Transaction(
    val description: String,

    @ManyToOne
    val account: Account,

    @ManyToOne
    val category: Category,

    val amount: Long,

    val confirmedAmount: Long?,

    val date: LocalDate,

    val confirmedDate: LocalDate?,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
)