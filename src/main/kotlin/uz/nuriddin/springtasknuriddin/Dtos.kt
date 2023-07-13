package uz.nuriddin.springtasknuriddin

import java.math.BigDecimal
import java.util.*

data class BaseMessage(val code: Int, val message: String?)


data class UserCreateDto(
    val balance: BigDecimal,
    val userName: String,
    val fullName: String,
) {
    fun toEntity() = User(userName, fullName, balance)
}


data class UserUpdateDto(
    val balance: BigDecimal?,
    val userName: String?,
    val fullName: String?,
)

data class GetOneUserDto(
    val id: Long?,
    val balance: BigDecimal,
    val userName: String,
    val fullName: String
) {
    companion object {
        fun toDto(user: User): GetOneUserDto {
            return user.run {
                GetOneUserDto(id, balance, userName, fullName)
            }
        }
    }
}


/*   User transaction payment     */

//data class UserPaymentTransActionCreateDto(
//    val amount: BigDecimal,
//    val userId: Long?
//) {
//    companion object {
//        fun toEntity(user: User?) = UserPaymentTransAction();
//    }
//}

//data class UserPaymentTransActionUpdateDto(
//    val id:Long?,
//    val amount:BigDecimal,
//    val
//) {
//
//}
//
//data class GetOneUserTransActionPaymentsDto() {
//
//}


/*
*
*
* Category dto
*
* */


data class CategoryCreateDto(
    val name: String,
    val order: Long,
    val description: String
) {
    fun toEntity() = Category(name, order, description)
}

data class CategoryUpdateDto(
    val name: String,
    val order: Long,
    val description: String
)

data class GetOneCategoryDto(
    val id: Long?,
    val name: String,
    val order: Long,
    val description: String?
) {
    companion object {
        fun toDto(category: Category): GetOneCategoryDto {
            category.run {
                return GetOneCategoryDto(id, name, order, description)
            }
        }
    }
}


/* Product */
data class ProductCreateDto(
    val name: String,
    val count: Long,
    val description: String,
    val categoryId: Long
) {
    fun toEntity(category: Category) = Product(name, count, description, category)
}

data class ProductUpdateDto(
    val name: String,
    val count: Long,
    val description: String,
    val categoryId: Long?
)

data class GetOneProductDto(
    val id: Long?,
    val name: String,
    val count: Long,
    val description: String?,
    val categoryId: Long?
) {
    companion object {

        fun toDto(product: Product): GetOneProductDto {
            product.run {
                return GetOneProductDto(id, name, count, description, category.id)
            }
        }

    }
}


data class UserPaymentTransactionCreateDTO(
    val amount: BigDecimal,
    val date: Date,
    val userId: Long
) {
    fun toEntity(user: User) = UserPaymentTransAction(user, amount, date)
}

data class GetOneUserTransactionPaymentDTO(
    val userId: Long
)

data class UserPaymentTransactionCreateDto(
    val userId: Long?,
    val date: Date,
    val amount: BigDecimal
) {
    fun toEntity(user: User) = UserPaymentTransAction(user, amount, date)
}

data class UserPaymentTransactionUpdateDto(
    val amount: BigDecimal?
)

data class GetOneUserPaymentTransactionDto(
    val amount: BigDecimal,
    var date: Date?,
    val username: String?,
) {
    companion object {
        fun toDto(userPaymentTransaction: UserPaymentTransAction): GetOneUserPaymentTransactionDto {
            return userPaymentTransaction.run {
                GetOneUserPaymentTransactionDto(amount, date, user?.userName)
            }
        }
    }
}


data class PaymentDTO(
    val count: Long,
    val price: BigDecimal,
    val productId: Long,
    val date: Date,
    val userId: Long
) {


}

data class GetOnePaymentDTO(
    val count: Long,
    val amount: BigDecimal,
    val totalAmount: BigDecimal,
    val productId: Long?,
) {
    companion object {
        fun toDTO(transactionItem: TransactionItem): GetOnePaymentDTO {
            transactionItem.run {
                return GetOnePaymentDTO(count, amount, totalAmount, product.id)
            }
        }
    }
}

data class GetOneTransactionDTO(
    val totalAmount: BigDecimal,
    val date: Date,
    val userId: Long?
) {
    companion object {
        fun toDTO(transaction: Transaction): GetOneTransactionDTO {
            transaction.run {
                return GetOneTransactionDTO(totalAmount, date, user.id)
            }
        }
    }
}

data class TransactionItemCreateDto(
    val productId: Long? = null,
    val count: Long,
    val amount: BigDecimal,
    val totalAmount: BigDecimal,
    val transactionId: Long?

) {
    fun toEntity(product: Product?, transaction: Transaction?) =
        product?.let {
            if (transaction != null) {
                TransactionItem(it, count, amount, totalAmount, transaction)
            }
        }
}

data class TransactionItemUpdateDto(
    val count: Long?,
    val amount: BigDecimal?,
    val totalAmount: BigDecimal?,
)

data class GetOneTransactionItemDto(
    val productName: String?,
    val count: Long,
    val amount: BigDecimal,
    val totalAmount: BigDecimal,
    val transaction: Transaction?
) {
    companion object {
        fun toDto(transactionItem: TransactionItem): GetOneTransactionItemDto {
            return transactionItem.run {
                GetOneTransactionItemDto(product?.name, count, amount, totalAmount, transaction)
            }
        }
    }
}






