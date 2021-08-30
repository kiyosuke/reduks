package com.kiyosuke.reduks

interface Middleware<S, A> {

    suspend fun apply(store: Store<S, A>, action: A, next: Dispatcher<A>)
}

fun <S, A> middleware(block: suspend (store: Store<S, A>, action: A, next: Dispatcher<A>) -> Unit): Middleware<S, A> {
    return object : Middleware<S, A> {
        override suspend fun apply(store: Store<S, A>, action: A, next: Dispatcher<A>) {
            block(store, action, next)
        }
    }
}