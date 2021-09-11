package com.kiyosuke.common

import com.github.kiyosuke.reduks.Middleware

fun <S> logger() : Middleware<S> = Middleware { store, action, next ->
    println("Logger: [before] action: $action, state: ${store.state.value}")
    next(action)
    println("Logger: [after] action: $action, state: ${store.state.value}")
}