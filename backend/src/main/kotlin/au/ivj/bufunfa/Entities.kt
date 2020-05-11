package au.ivj.bufunfa

import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(
    indexes = [
        Index(name = "nameIdx", columnList = "name", unique = true)
    ]
)
class Category(
    @Column(unique = true)
    val name: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
)

@Entity
@Table(
    indexes = [
        Index(name = "nameIdx", columnList = "name", unique = true)
    ]
)
class Account(
    @Column(unique = true)
    val name: String,

    val initialAmount: Long,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
)

@Entity
@Table(
    indexes = [
        Index(name = "descriptionIdx", columnList = "description"),
        Index(name = "accountIdx", columnList = "account_id"),
        Index(name = "categoryIdx", columnList = "category_id"),
        Index(name = "dateIdx", columnList = "date")
    ]
)
class Transaction(
    val description: String,

    @ManyToOne
    val account: Account,

    @ManyToOne
    val category: Category,

    val amount: Long,

    val date: LocalDate,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
)