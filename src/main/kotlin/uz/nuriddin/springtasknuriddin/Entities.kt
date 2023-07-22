package uz.nuriddin.springtasknuriddin

import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.math.BigDecimal
import java.util.Date


@MappedSuperclass
class BaseEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    @CreatedDate @Temporal(TemporalType.TIMESTAMP) var createdDate: Date? = null,
    @LastModifiedDate @Temporal(TemporalType.TIMESTAMP) var modifiedDate: Date? = null,
    @Column(nullable = false) @ColumnDefault(value = "false") var deleted: Boolean = false,
)

@Entity(name = "users")
class User(
    var fullName: String,
    @Column(unique = true) var username: String,
    var balance: BigDecimal
) : BaseEntity()

@Entity
class UserPaymentTransaction(
    var amount: BigDecimal,
    var date: Date,
    @ManyToOne var user: User
) : BaseEntity()

@Entity
class Category(
    var name: String,
    var orders: Long,
    @Column(length = 128) var description: String? = null
) : BaseEntity()

@Entity
class Product(
    var name: String,
    var count: Long,
    @ManyToOne var category: Category
) : BaseEntity()

@Entity
class Transaction(
    var totalPrice: BigDecimal,
    var date: Date,
    @ManyToOne var user: User
) : BaseEntity()

@Entity
class TransactionItem(
    var count: Long,
    var price: BigDecimal,
    var totalPrice: BigDecimal,
    @ManyToOne var product: Product,
    @ManyToOne var transaction: Transaction
) : BaseEntity()