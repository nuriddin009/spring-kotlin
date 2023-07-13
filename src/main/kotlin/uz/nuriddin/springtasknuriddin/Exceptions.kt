package uz.nuriddin.springtasknuriddin

import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.context.support.ResourceBundleMessageSource
import java.math.BigDecimal
import java.util.*

sealed class NuriddinTaskException(message: String? = null) : RuntimeException(message) {
    abstract fun errorType(): ErrorCode

    fun getErrorMessage(errorMessageSource: ResourceBundleMessageSource, vararg array: Any?): BaseMessage {
        return BaseMessage(
            errorType().code,
            errorMessageSource.getMessage(
                errorType().toString(),
                array,
                Locale(LocaleContextHolder.getLocale().language)
            )
        )
    }
}

class UserNameExistsException(val userName: String) : NuriddinTaskException() {
    override fun errorType() = ErrorCode.USER_NAME_EXISTS
}

class TransactionNotFoundException(val id: Long) : NuriddinTaskException() {
    override fun errorType(): ErrorCode = ErrorCode.TRANSACTION_NOT_FOUND
}

class CategoryNotFoundException(val id: Long) : NuriddinTaskException() {
    override fun errorType(): ErrorCode = ErrorCode.CATEGORY_NOT_FOUND
}

class UserNotFoundException(val id: Long) : NuriddinTaskException() {
    override fun errorType() = ErrorCode.USER_NOT_FOUND
}

class BalanceNotEnoughException(val balance: BigDecimal) : NuriddinTaskException() {
    override fun errorType() = ErrorCode.BALANCE_NOT_ENOUGH
}

class ProductFoundException(val id: Long) : NuriddinTaskException() {
    override fun errorType() = ErrorCode.PRODUCT_NOT_FOUND
}

class TransactionItemNotFoundException(val id: Long) : NuriddinTaskException() {
    override fun errorType() = ErrorCode.TRANSACTION_ITEM_NOT_FOUND
}



