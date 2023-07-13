package uz.nuriddin.springtasknuriddin

import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*


interface UserService {
    fun create(dto: UserCreateDto)
    fun update(id: Long, dto: UserUpdateDto)
    fun getOne(id: Long)
    fun getAll(pageable: Pageable): Page<GetOneUserDto>
    fun delete(id: Long)

    fun addBalance(id: Long, money: BigDecimal)
}

interface CategoryService {
    fun create(dto: CategoryCreateDto)
    fun update(id: Long, dto: CategoryUpdateDto)
    fun getOne(id: Long): GetOneCategoryDto
    fun getAll(pageable: Pageable): Page<GetOneCategoryDto>
    fun delete(id: Long)
}

interface ProductService {
    fun create(dto: ProductCreateDto)
    fun update(id: Long, dto: ProductUpdateDto)
    fun getOne(id: Long): GetOneProductDto
    fun getAll(pageable: Pageable): Page<GetOneProductDto>
    fun delete(id: Long)
}


interface UserPaymentTransactionService {
    fun getAllByUser(id: Long, pageable: Pageable): Page<GetOneUserTransactionPaymentDTO>
}

interface TransactionItemService {
    fun create(dto: TransactionItemCreateDto)
    fun update(id: Long, dto: TransactionItemUpdateDto)
    fun getOne(id: Long): GetOneTransactionItemDto
    fun getAll(pageable: Pageable): Page<GetOneTransactionItemDto>
    fun delete(id: Long)
}


@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val userPaymentTransActionRepository: UserPaymentTransActionRepository
) : UserService {
    override fun create(dto: UserCreateDto) {
        dto.run {
            if (userRepository.existsByUserName(userName)) throw UserNameExistsException(userName)
            userRepository.save(toEntity())
        }
    }

    override fun update(id: Long, dto: UserUpdateDto) {

        val user = userRepository.findByIdAndDeletedFalse(id) ?: throw UserNotFoundException(id)

        dto.run {
            fullName?.let { user.fullName = it }
            userName?.let { user.userName = it }
            balance?.let { user.balance = it }
        }

        userRepository.save(user)

    }

    override fun getOne(id: Long) {
        userRepository.findByIdAndDeletedFalse(id)?.let { GetOneUserDto.toDto(it) }
    }

    override fun getAll(pageable: Pageable): Page<GetOneUserDto> {
        return userRepository.findAllNotDeleted(pageable).map { GetOneUserDto.toDto(it) }
    }

    override fun delete(id: Long) {
        userRepository.trash(id) ?: UserNotFoundException(id)
    }

    override fun addBalance(id: Long, money: BigDecimal) {
        val user = userRepository.findByIdAndDeletedFalse(id) ?: throw UserNotFoundException(id)
        user?.let { user.balance = user.balance + money }
        userRepository.save(user)
        val userPaymentTransAction = UserPaymentTransAction(user, money, date = Date())
        userPaymentTransActionRepository.save(userPaymentTransAction)
    }

}

@Service
class CategoryServiceImpl(
    private val categoryRepository: CategoryRepository
) : CategoryService {


    override fun create(dto: CategoryCreateDto) {
        dto.run { categoryRepository.save(toEntity()) }
    }

    override fun update(id: Long, dto: CategoryUpdateDto) {
        val category = categoryRepository.findByIdAndDeletedFalse(id) ?: throw CategoryNotFoundException(id)
        dto.run {
            name?.let { category.name = it }
            order?.let { category.order = it }
            description?.let { category.description = it }
        }
        categoryRepository.save(category)
    }

    override fun getOne(id: Long): GetOneCategoryDto {
        return categoryRepository.findByIdAndDeletedFalse(id)?.let { GetOneCategoryDto.toDto(it) }
            ?: throw CategoryNotFoundException(id)
    }

    override fun getAll(pageable: Pageable): Page<GetOneCategoryDto> {
        return categoryRepository.findAllNotDeleted(pageable).map { GetOneCategoryDto.toDto(it) }
    }

    override fun delete(id: Long) {
        categoryRepository.trash(id) ?: throw CategoryNotFoundException(id)
    }

}


@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository,
    private val entityManager: EntityManager
) : ProductService {

    @Transactional
    override fun create(dto: ProductCreateDto) {
        dto.run {
            val category = categoryId.let {
                categoryRepository.existsByIdAndDeletedFalse(it).runIfFalse { throw CategoryNotFoundException(it) }
                entityManager.getReference(Category::class.java, it)
            }
            productRepository.save(toEntity(category))
        }
    }

    override fun update(id: Long, dto: ProductUpdateDto) {
        val product = productRepository.findByIdAndDeletedFalse(id) ?: throw ProductFoundException(id)
        dto.run {
            val category = categoryId?.let {
                categoryRepository.existsByIdAndDeletedFalse(it).runIfFalse { throw CategoryNotFoundException(it) }
                entityManager.getReference(Category::class.java, it)
            }
            name?.let { product.name = it }
            count?.let { product.count = count }
            category?.let { product.category = it }
            productRepository.save(product)
        }
    }

    override fun getOne(id: Long): GetOneProductDto {
        return productRepository.findByIdAndDeletedFalse(id)?.let { GetOneProductDto.toDto(it) }
            ?: throw ProductFoundException(id)
    }

    override fun getAll(pageable: Pageable): Page<GetOneProductDto> {
        return productRepository.findAllNotDeleted(pageable).map { GetOneProductDto.toDto(it) }
    }

    override fun delete(id: Long) {
        productRepository.trash(id) ?: throw ProductFoundException(id)
    }

}

@Service
class UserPaymentTransactionServiceImpl(
    private val userPaymentTransActionRepository: UserPaymentTransActionRepository,
    private val userRepository: UserRepository
) : UserPaymentTransactionService {
    override fun getAllByUser(id: Long, pageable: Pageable): Page<GetOneUserTransactionPaymentDTO> {
       return userPaymentTransActionRepository.findAllByUserIdAndDeletedFalse(id,pageable)
    }
}

//@Service
//class TransactionItemServiceImpl(
//    private val transActionRepository: UserPaymentTransActionRepository,
//    private val productRepository: ProductRepository,
//    private val transActionRepository: UserPaymentTransActionRepository,
//    private val entityManager: EntityManager
//) : TransactionItemService {
//
//
//    @Transactional
//    override fun create(dto: TransactionItemCreateDto) {
//        dto.run {
//            val product = productId?.let {
//                productRepository.existsByIdAndDeletedFalse(it).runIfFalse { throw ProductFoundException(it) }
//                entityManager.getReference(Product::class.java, it)
//            }
//            val transaction = transactionId?.let {
//                transActionRepository.existsByIdAndDeletedFalse(it).runIfFalse { throw ProductFoundException(it) }
//                entityManager.getReference(Transaction::class.java, it)
//            }
//            transActionRepository.save(toEntity(product, transaction))
//        }
//    }
//
//    override fun update(id: Long, dto: TransactionItemUpdateDto) {
//        val transactionItem =
//            transactionItemRepository.findByIdAndDeletedFalse(id) ?: throw TransactionItemNotFoundException(id)
////        dto.run {
////            count?.let { transactionItem.count = it }
////            amount?.let { transactionItem.amount = it }
////            totalAmount?.let { transactionItem.total = it }
////        }
//        transactionItemRepository.save(transactionItem)
//    }
//
//    override fun getOne(id: Long) =
//        transactionItemRepository.findByIdAndDeletedFalse(id)?.let { GetOneTransactionItemDto.toDto(it) }
//            ?: throw TransactionItemNotFoundException(id)
//
//    override fun getAll(pageable: Pageable) =
//        transactionItemRepository.findAllNotDeleted(pageable).map { GetOneTransactionItemDto.toDto(it) }
//
//    override fun delete(id: Long) {
//        transactionItemRepository.trash(id) ?: throw TransactionItemNotFoundException(id)
//    }
//
//}




