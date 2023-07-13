package uz.nuriddin.springtasknuriddin

import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.math.BigDecimal
import java.util.*


@MappedSuperclass
class BaseEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    @CreatedDate @Temporal(TemporalType.TIMESTAMP) var createdDate: Date? = null,
    @LastModifiedDate @Temporal(TemporalType.TIMESTAMP) var modifiedDate: Date? = null,
    @Column(nullable = false) @ColumnDefault(value = "false") var deleted: Boolean = false,
)


@Entity(name = "users")
class User(
    @Column(length = 64, unique = true) var userName: String,
    @Column(length = 128) var fullName: String,
    var balance: BigDecimal
) : BaseEntity()


@Entity(name = "user_payment_transaction")
class UserPaymentTransAction(
    @ManyToOne @JoinColumn(name = "user_id") var user: User,
    var amount: BigDecimal,
    var date: Date
) : BaseEntity()


@Entity
class Transaction(
    @ManyToOne var user: User,
    var totalAmount: BigDecimal,
    var date: Date
) : BaseEntity()


@Entity
class TransactionItem(
    @ManyToOne var product: Product,
    var count: Long,
    var amount: BigDecimal,
    var totalAmount: BigDecimal,
    @ManyToOne var transaction: Transaction
) : BaseEntity()


@Entity
class Product(
    var name: String,
    var count: Long,
    var description: String?,
    @ManyToOne var category: Category
) : BaseEntity()


@Entity
class Category(
    var name: String,
    @Column(name = "orders") var order: Long,
    var description: String?
) : BaseEntity()








