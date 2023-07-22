package uz.nuriddin.springtasknuriddin

import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@ControllerAdvice
class ExceptionHandlers(
    private val errorMessageSource: ResourceBundleMessageSource
) {
    @ExceptionHandler(DemoException::class)
    fun handleException(exception: DemoException): ResponseEntity<*> {
        return when (exception) {
            is UsernameExistsException -> ResponseEntity.badRequest()
                .body(exception.getErrorMessage(errorMessageSource, exception.username))

            is UserNotFoundException -> ResponseEntity.badRequest()
                .body(exception.getErrorMessage(errorMessageSource, exception.id))

            is UserPaymentTransactionNotFoundException -> ResponseEntity.badRequest()
                .body(exception.getErrorMessage(errorMessageSource, exception.id))

            is CategoryNotFoundException -> ResponseEntity.badRequest()
                .body(exception.getErrorMessage(errorMessageSource, exception.id))

            is ProductNotFoundException -> ResponseEntity.badRequest()
                .body(exception.getErrorMessage(errorMessageSource, exception.id))

            is ProductIsNotEnoughException -> ResponseEntity.badRequest()
                .body(exception.getErrorMessage(errorMessageSource, exception.count))

            is BalanceIsNotEnoughException -> ResponseEntity.badRequest()
                .body(exception.getErrorMessage(errorMessageSource, exception.price))

            is TransactionNotFoundException -> ResponseEntity.badRequest()
                .body(exception.getErrorMessage(errorMessageSource, exception.id))
        }
    }
}

@RestController
@RequestMapping("api/v1/user")
class UserController(private val service: UserService) {

    @PostMapping
    fun create(@RequestBody dto: UserCreateDTO) = service.create(dto)

    @PutMapping("{id}")
    fun update(@PathVariable id: Long, @RequestBody dto: UserUpdateDTO) = service.update(id, dto)

    @GetMapping("{id}")
    fun getOne(@PathVariable id: Long): GetOneUserDTO = service.getOne(id)

    @GetMapping
    fun getAll(pageable: Pageable): Page<GetOneUserDTO> = service.getAll(pageable)

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long) = service.delete(id)

}

@RestController
@RequestMapping("api/v1/userPaymentTransaction")
class UserPaymentTransactionController(private val service: UserPaymentTransactionService) {

    @PostMapping
    fun payment(@RequestBody dto: UserPaymentTransactionCreateDTO) =
        service.payment(dto)

    @GetMapping
    fun getPayments(pageable: Pageable): Page<GetOneUserPaymentTransactionDTO> =
        service.getPayments(pageable)

}

@RestController
@RequestMapping("api/v1/transaction")
class TransactionController(private val service: TransactionService) {

    @GetMapping
    fun getAll(pageable: Pageable): Page<GetOneTransactionDTO> =
        service.getAll(pageable)

}

@RestController
@RequestMapping("api/v1/transactionItem")
class TransactionItemController(private val service: TransactionItemService) {

    @PostMapping
    fun purchase(@RequestBody dto: PurchaseDTO) = service.purchase(dto)

    @GetMapping("user/{userId}")
    fun getPurchasesByUser(pageable: Pageable, @PathVariable userId: Long): Page<GetOnePurchaseDTO> =
        service.getPurchasesByUser(pageable, userId)

    @GetMapping("transaction/{transactionId}")
    fun getPurchasesByTransaction(
        pageable: Pageable,
        @PathVariable transactionId: Long
    ): Page<GetOnePurchaseDTO> = service.getPurchasesByTransaction(
        pageable,
        transactionId
    )

}



@RestController
@RequestMapping("api/v1/category")
class CategoryController(private val service: CategoryService) {

    @PostMapping
    fun create(@RequestBody dto: CategoryCreateDTO) = service.create(dto)

    @PutMapping("{id}")
    fun update(@PathVariable id: Long, @RequestBody dto: CategoryUpdateDTO) = service.update(id, dto)

    @GetMapping("{id}")
    fun getOne(@PathVariable id: Long): GetOneCategoryDTO = service.getOne(id)

    @GetMapping
    fun getAll(pageable: Pageable): Page<GetOneCategoryDTO> = service.getAll(pageable)

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long) = service.delete(id)

}

@RestController
@RequestMapping("api/v1/product")
class ProductController(private val service: ProductService) {

    @PostMapping
    fun create(@RequestBody dto: ProductCreateDTO) = service.create(dto)

    @PutMapping("{id}")
    fun update(@PathVariable id: Long, @RequestBody dto: ProductUpdateDTO) = service.update(id, dto)

    @GetMapping("{id}")
    fun getOne(@PathVariable id: Long): GetOneProductDTO = service.getOne(id)

    @GetMapping
    fun getAll(pageable: Pageable): Page<GetOneProductDTO> = service.getAll(pageable)

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long) = service.delete(id)

}

