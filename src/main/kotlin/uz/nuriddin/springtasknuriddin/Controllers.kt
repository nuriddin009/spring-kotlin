package uz.nuriddin.springtasknuriddin

import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal


@ControllerAdvice
class ExceptionHandlers(
    private val errorMessageSource: ResourceBundleMessageSource
) {
    @ExceptionHandler(NuriddinTaskException::class)
    fun handleException(exception: NuriddinTaskException): ResponseEntity<*> {
        return when (exception) {
            is UserNameExistsException -> ResponseEntity.badRequest()
                .body(exception.getErrorMessage(errorMessageSource, exception.userName))

            is ProductFoundException -> ResponseEntity.badRequest()
                .body(exception.getErrorMessage(errorMessageSource, exception.id))

            is CategoryNotFoundException -> ResponseEntity.badRequest()
                .body(exception.getErrorMessage(errorMessageSource, exception.id))

            is UserNotFoundException -> ResponseEntity.badRequest()
                .body(exception.getErrorMessage(errorMessageSource, exception.id))

            is BalanceNotEnoughException -> ResponseEntity.badRequest()
                .body(exception.getErrorMessage(errorMessageSource, exception.balance))

            is TransactionNotFoundException -> ResponseEntity.badRequest()
                .body(exception.getErrorMessage(errorMessageSource, exception.id))

            is TransactionItemNotFoundException -> ResponseEntity.badRequest()
                .body(exception.getErrorMessage(errorMessageSource, exception.id))

        }
    }
}

@RestController
@RequestMapping("api/v1/user")
class UserController(private val service: UserService) {

    @PostMapping
    fun create(@RequestBody dto: UserCreateDto) = service.create(dto)

    @PutMapping("{id}")
    fun update(@PathVariable id: Long, @RequestBody dto: UserUpdateDto) = service.update(id, dto)

    @PutMapping("{id}")
    fun addBalanceToUser(@PathVariable id: Long, @RequestBody money: BigDecimal) = service.addBalance(id, money)

    @GetMapping("{id}")
    fun getOne(@PathVariable id: Long): Unit = service.getOne(id)

    @GetMapping
    fun getAll(pageable: Pageable): Page<GetOneUserDto> = service.getAll(pageable)

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long) = service.delete(id)
}


@RestController
@RequestMapping("api/v1/category")
class CategoryController(private val service: CategoryService) {

    @PostMapping
    fun create(@RequestBody dto: CategoryCreateDto) = service.create(dto)

    @PutMapping("{id}")
    fun update(@PathVariable id: Long, @RequestBody dto: CategoryUpdateDto) = service.update(id, dto)

    @GetMapping("{id}")
    fun getOne(@PathVariable id: Long): GetOneCategoryDto = service.getOne(id)

    @GetMapping
    fun getAll(pageable: Pageable): Page<GetOneCategoryDto> = service.getAll(pageable)

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long) = service.delete(id)
}

@RestController
@RequestMapping("api/v1/product")
class ProductController(private val service: ProductService) {

    @PostMapping
    fun create(@RequestBody dto: ProductCreateDto) = service.create(dto)

    @PutMapping("{id}")
    fun update(@PathVariable id: Long, @RequestBody dto: ProductUpdateDto) = service.update(id, dto)

    @GetMapping("{id}")
    fun getOne(@PathVariable id: Long): GetOneProductDto = service.getOne(id)

    @GetMapping
    fun getAll(pageable: Pageable): Page<GetOneProductDto> = service.getAll(pageable)

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long) = service.delete(id)

}

//@RestController
//@RequestMapping("api/v1/transactionItem")
//class TransactionItemController(private val service: TransactionItemService) {
//
//    @PostMapping
//    fun create(@RequestBody dto: TransactionItemCreateDto) = service.create(dto)
//
//    @PutMapping("{id}")
//    fun update(@PathVariable id: Long, @RequestBody dto: TransactionItemUpdateDto) = service.update(id, dto)
//
//    @GetMapping("{id}")
//    fun getOne(@PathVariable id: Long): GetOneTransactionItemDto = service.getOne(id)
//
//    @GetMapping
//    fun getAll(pageable: Pageable): Page<GetOneTransactionItemDto> = service.getAll(pageable)
//
//    @DeleteMapping("{id}")
//    fun delete(@PathVariable id: Long) = service.delete(id)
//}






