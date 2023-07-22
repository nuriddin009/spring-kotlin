package uz.nuriddin.springtasknuriddin

import java.math.BigDecimal
import java.util.*

data class BaseMessage(val code: Int, val message: String?)
data class UserCreateDTO(
    val fullName: String,
    val username: String,
) {
    fun toEntity() = User(fullName, username, BigDecimal.valueOf(0))
}

data class UserUpdateDTO(
    val fullName: String?,
    val username: String?,
    val balance: BigDecimal?
)

data class GetOneUserDTO(
    val id: Long?,
    val fullName: String,
    val username: String,
    val balance: BigDecimal
) {
    companion object {
        fun toDTO(user: User): GetOneUserDTO {
            return user.run {
                GetOneUserDTO(id, fullName, username, balance)
            }
        }
    }
}


data class UserPaymentTransactionCreateDTO(
    val amount: BigDecimal,
    val date: Date,
    val userId: Long
) {
    fun toEntity(user: User) = UserPaymentTransaction(amount, date, user)
}

data class GetOneUserPaymentTransactionDTO(
    val id: Long?,
    val amount: BigDecimal,
    val date: Date,
    val userId: Long?
) {
    companion object {
        fun toDTO(userPaymentTransaction: UserPaymentTransaction): GetOneUserPaymentTransactionDTO {
            userPaymentTransaction.run {
                return GetOneUserPaymentTransactionDTO(id, amount, date, user.id)
            }
        }
    }
}


data class CategoryCreateDTO(
    val name: String,
    val orders: Long,
    val description: String?
) {
    fun toEntity() = Category(name, orders, description)
}

data class CategoryUpdateDTO(
    val name: String?,
    val orders: Long?,
    val description: String?
)

data class GetOneCategoryDTO(
    val id: Long?,
    val name: String,
    val orders: Long,
    val description: String?
) {
    companion object {
        fun toDTO(category: Category): GetOneCategoryDTO {
            category.run {
                return GetOneCategoryDTO(id, name, orders, description)
            }
        }
    }
}


data class ProductCreateDTO(
    val name: String,
    val count: Long,
    val categoryId: Long
) {
    fun toEntity(category: Category) = Product(name, count, category)
}

data class ProductUpdateDTO(
    val name: String?,
    val count: Long?,
    val categoryId: Long?
)

data class GetOneProductDTO(
    val id: Long?,
    val name: String,
    val count: Long,
    val categoryId: Long?
) {
    companion object {
        fun toDTO(product: Product): GetOneProductDTO {
            product.run {
                return GetOneProductDTO(id, name, count, category.id)
            }
        }
    }
}


data class PurchaseDTO(
    val count: Long,
    val price: BigDecimal,
    val productId: Long,
    val date: Date,
    val userId: Long
) {
    fun toTransactionEntity(user: User) = Transaction(BigDecimal.valueOf(count) * price, date, user)
    fun toTransactionItemEntity(product: Product, transaction: Transaction) =
        TransactionItem(count, price, BigDecimal.valueOf(count) * price, product, transaction)
}

data class GetOnePurchaseDTO(
    val count: Long,
    val price: BigDecimal,
    val totalPrice: BigDecimal,
    val productId: Long?,
) {
    companion object {
        fun toDTO(transactionItem: TransactionItem): GetOnePurchaseDTO {
            transactionItem.run {
                return GetOnePurchaseDTO(count, price, totalPrice, product.id)
            }
        }
    }
}

data class GetOneTransactionDTO(
    val totalPrice: BigDecimal,
    val date: Date,
    val userId: Long?
) {
    companion object {
        fun toDTO(transaction: Transaction): GetOneTransactionDTO {
            transaction.run {
                return GetOneTransactionDTO(totalPrice, date, user.id)
            }
        }
    }
}