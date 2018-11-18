package com.rxdroid.common

data class Consumable<T>(val data: T) {
    private var consumed: Boolean = false

    fun isConsumed() = consumed

    fun consume() {
        consumed = true
    }
}

inline infix fun <T> Consumable<T>?.invokeIfNeeded(closure: (T) -> Unit) {
    if (this != null && !this.isConsumed()) {
        consume()
        closure(data)
    }
}