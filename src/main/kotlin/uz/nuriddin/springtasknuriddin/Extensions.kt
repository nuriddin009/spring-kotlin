package uz.nuriddin.springtasknuriddin

fun Boolean.runIfFalse(func: () -> Unit) {
    if (!this) func()
}
