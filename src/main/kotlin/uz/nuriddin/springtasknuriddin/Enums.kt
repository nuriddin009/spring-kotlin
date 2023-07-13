package uz.nuriddin.springtasknuriddin

enum class ErrorCode(val code: Int) {
    USER_NAME_EXISTS(100),
    TRANSACTION_NOT_FOUND(101),
    CATEGORY_NOT_FOUND(102),
    USER_NOT_FOUND(103),
    BALANCE_NOT_ENOUGH(104),
    PRODUCT_NOT_FOUND(105),
    USER_TRANSACTION_PAYMENT_NOT_FOUND(106),
    TRANSACTION_ITEM_NOT_FOUND(107)
}