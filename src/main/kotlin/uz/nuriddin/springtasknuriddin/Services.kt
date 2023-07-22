package uz.nuriddin.springtasknuriddin

import jakarta.persistence.EntityManager
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.math.BigDecimal

interface UserService {
    fun create(dto: UserCreateDTO)
    fun update(id: Long, dto: UserUpdateDTO)
    fun getOne(id: Long): GetOneUserDTO
    fun getAll(pageable: Pageable): Page<GetOneUserDTO>
    fun delete(id: Long)
}

interface UserPaymentTransactionService {
    fun payment(dto: UserPaymentTransactionCreateDTO)
    fun getPayments(pageable: Pageable): Page<GetOneUserPaymentTransactionDTO>
}

interface CategoryService {
    fun create(dto: CategoryCreateDTO)
    fun update(id: Long, dto: CategoryUpdateDTO)
    fun getOne(id: Long): GetOneCategoryDTO
    fun getAll(pageable: Pageable): Page<GetOneCategoryDTO>
    fun delete(id: Long)
}

interface ProductService {
    fun create(dto: ProductCreateDTO)
    fun update(id: Long, dto: ProductUpdateDTO)
    fun getOne(id: Long): GetOneProductDTO
    fun getAll(pageable: Pageable): Page<GetOneProductDTO>
    fun delete(id: Long)
}

interface TransactionItemService {
    fun purchase(dto: PurchaseDTO)
    fun getPurchasesByUser(pageable: Pageable, userId: Long): Page<GetOnePurchaseDTO>
    fun getPurchasesByTransaction(pageable: Pageable, transactionId: Long): Page<GetOnePurchaseDTO>
}

interface TransactionService {
    fun getAll(pageable: Pageable): Page<GetOneTransactionDTO>
}

@Service
class UserServiceImpl(
    private val userRepo: UserRepository
) : UserService {
    override fun create(dto: UserCreateDTO) {
        dto.run {
            if (userRepo.existsByUsername(username)) throw UsernameExistsException(username)
            userRepo.save(toEntity())
        }
    }

    override fun update(id: Long, dto: UserUpdateDTO) {
        val user = userRepo.findByIdAndDeletedFalse(id) ?: throw UserNotFoundException(id)
        dto.run {
            if (username?.let { userRepo.existsByUsername(it) } == true) throw UsernameExistsException(username)
            fullName?.let { user.fullName = it }
            username?.let { user.username = it }
            balance?.let { user.balance = it }
        }
        userRepo.save(user)
    }

    override fun getOne(id: Long) =
        userRepo.findByIdAndDeletedFalse(id)?.let { GetOneUserDTO.toDTO(it) } ?: throw UserNotFoundException(id)


    override fun getAll(pageable: Pageable) = userRepo.findAllNotDeleted(pageable).map { GetOneUserDTO.toDTO(it) }

    override fun delete(id: Long) {
        userRepo.trash(id) ?: throw UserNotFoundException(id)
    }

}

@Service
class UserPaymentTransactionServiceImpl(
    private val userPaymentTransactionRepo: UserPaymentTransactionRepository,
    private val userRepo: UserRepository,
) : UserPaymentTransactionService {
    override fun payment(dto: UserPaymentTransactionCreateDTO) {
        dto.run {
            val user = userId.let {
                userRepo.findByIdAndDeletedFalse(it) ?: throw UserNotFoundException(userId)
            }
            val savedUser = user.run {
                balance += amount
                userRepo.save(user)
            }
            userPaymentTransactionRepo.save(toEntity(savedUser))
        }
    }

    override fun getPayments(pageable: Pageable): Page<GetOneUserPaymentTransactionDTO> =
        userPaymentTransactionRepo.findAllNotDeleted(pageable).map { GetOneUserPaymentTransactionDTO.toDTO(it) }

}

@Service
class CategoryServiceImpl(
    private val categoryRepo: CategoryRepository
) : CategoryService {

    override fun create(dto: CategoryCreateDTO) {
        dto.run {
            categoryRepo.save(toEntity())
        }
    }

    override fun update(id: Long, dto: CategoryUpdateDTO) {
        val category = categoryRepo.findByIdAndDeletedFalse(id) ?: throw CategoryNotFoundException(id)
        dto.run {
            name?.let { category.name = it }
            orders?.let { category.orders = it }
            description?.let { category.description = it }
        }
        categoryRepo.save(category)
    }

    override fun getOne(id: Long) = categoryRepo.findByIdAndDeletedFalse(id)?.let { GetOneCategoryDTO.toDTO(it) }
        ?: throw CategoryNotFoundException(id)

    override fun getAll(pageable: Pageable) =
        categoryRepo.findAllNotDeleted(pageable).map { GetOneCategoryDTO.toDTO(it) }

    override fun delete(id: Long) {
        categoryRepo.trash(id) ?: throw CategoryNotFoundException(id)
    }

}

@Service
class ProductServiceImpl(
    private val productRepo: ProductRepository,
    private val categoryRepo: CategoryRepository,
    private val entityManager: EntityManager
) : ProductService {

    override fun create(dto: ProductCreateDTO) {
        dto.run {
            val category = categoryId.let {
                categoryRepo.existsByIdAndDeletedFalse(it).runIfFalse { throw CategoryNotFoundException(it) }
                entityManager.getReference(Category::class.java, it)
            }
            productRepo.save(toEntity(category))
        }
    }

    override fun update(id: Long, dto: ProductUpdateDTO) {
        val product = productRepo.findByIdAndDeletedFalse(id) ?: throw ProductNotFoundException(id)
        dto.run {
            val category = categoryId?.let {
                categoryRepo.existsByIdAndDeletedFalse(it).runIfFalse { throw CategoryNotFoundException(it) }
                entityManager.getReference(Category::class.java, it)
            }
            name?.let { product.name = it }
            count?.let { product.count = it }
            category?.let {
                product.category = it
            }
            productRepo.save(product)
        }
    }

    override fun getOne(id: Long) = productRepo.findByIdAndDeletedFalse(id)?.let { GetOneProductDTO.toDTO(it) }
        ?: throw ProductNotFoundException(id)

    override fun getAll(pageable: Pageable) = productRepo.findAllNotDeleted(pageable).map { GetOneProductDTO.toDTO(it) }

    override fun delete(id: Long) {
        productRepo.trash(id) ?: throw ProductNotFoundException(id)
    }

    @Service
    class TransactionItemServiceImpl(
        private val transactionItemRepo: TransactionItemRepository,
        private val transactionRepo: TransactionRepository,
        private val productRepo: ProductRepository,
        private val userRepo: UserRepository,
    ) : TransactionItemService {
        override fun purchase(dto: PurchaseDTO) {
            dto.run {
                var product = productId.let {
                    productRepo.findByIdAndDeletedFalse(it) ?: throw ProductNotFoundException(it)
                }
                var user = userId.let {
                    userRepo.findByIdAndDeletedFalse(it) ?: throw UserNotFoundException(it)
                }

                if (product.count < count)
                    throw ProductIsNotEnoughException(count)
                if (user.balance < BigDecimal.valueOf(count) * price)
                    throw BalanceIsNotEnoughException(BigDecimal.valueOf(count) * price)

                product = count.let {
                    product.count -= it
                    productRepo.save(product)
                }
                user = price.let {
                    user.balance -= it * BigDecimal.valueOf(count)
                    userRepo.save(user)
                }
                val transaction =
                    transactionRepo.findByDateAndUserIdAndDeletedFalse(date, userId)
                if (transaction != null)
                    transactionItemRepo.save(toTransactionItemEntity(product, transaction))
                else {
                    val newTransaction = transactionRepo.save(toTransactionEntity(user))
                    transactionItemRepo.save(toTransactionItemEntity(product, newTransaction))
                }
            }
        }

        override fun getPurchasesByUser(pageable: Pageable, userId: Long): Page<GetOnePurchaseDTO> {
            userRepo.existsById(userId).runIfFalse { throw UserNotFoundException(userId) }
            return transactionItemRepo.findAllByTransactionUserId(pageable, userId).map { GetOnePurchaseDTO.toDTO(it) }
        }

        override fun getPurchasesByTransaction(pageable: Pageable, transactionId: Long): Page<GetOnePurchaseDTO> {
            transactionRepo.existsById(transactionId).runIfFalse { throw TransactionNotFoundException(transactionId) }
            return transactionItemRepo.findAllByTransactionId(pageable, transactionId)
                .map { GetOnePurchaseDTO.toDTO(it) }
        }

    }

    @Service
    class TransactionServiceImpl(
        private val transactionRepo: TransactionRepository
    ) : TransactionService {
        override fun getAll(pageable: Pageable) =
            transactionRepo.findAllNotDeleted(pageable).map { GetOneTransactionDTO.toDTO(it) }
    }

}