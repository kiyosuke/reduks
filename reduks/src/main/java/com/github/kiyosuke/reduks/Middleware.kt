package com.github.kiyosuke.reduks

interface Middleware<S> {

    suspend fun apply(store: Store<S>, action: Action, next: Dispatcher<Action>)

    companion object {
        inline operator fun <S> invoke(crossinline block: suspend (store: Store<S>, action: Action, next: Dispatcher<Action>) -> Unit): Middleware<S> {
            return object : Middleware<S> {
                override suspend fun apply(store: Store<S>, action: Action, next: Dispatcher<Action>) {
                    block(store, action, next)
                }
            }
        }
    }
}