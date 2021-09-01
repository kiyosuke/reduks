package com.kiyosuke.reduks.middlewares

import com.kiyosuke.reduks.Dispatcher
import com.kiyosuke.reduks.Middleware
import com.kiyosuke.reduks.Store

private fun <S, A> middleware(block: suspend (store: Store<S, A>, action: A, next: Dispatcher<A>) -> Unit): Middleware<S, A> {
    return object : Middleware<S, A> {
        override suspend fun apply(store: Store<S, A>, action: A, next: Dispatcher<A>) {
            block(store, action, next)
        }
    }
}

fun <S, A> createMiddleware(block: suspend (getState: () -> S, action: A, next: Dispatcher<A>) -> Unit): Middleware<S, A> {
    return middleware { store, action, next -> block(store.state::value, action, next) }
}

fun <S, A> createMiddleware(
    predicate: (A) -> Boolean,
    block: suspend (Store<S, A>, action: A, next: Dispatcher<A>) -> Unit
): Middleware<S, A> {
    return middleware { store, action, next ->
        if (predicate(action)) {
            block(store, action, next)
        } else {
            next(action)
        }
    }
}