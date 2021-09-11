package com.github.kiyosuke.reduks.middlewares

import com.github.kiyosuke.reduks.Action
import com.github.kiyosuke.reduks.Dispatcher
import com.github.kiyosuke.reduks.Middleware
import com.github.kiyosuke.reduks.Store

inline fun <S, reified A : Action> middleware(crossinline block: suspend (store: Store<S>, action: A, next: Dispatcher<A>) -> Unit): Middleware<S> {
    return object : Middleware<S> {
        override suspend fun apply(store: Store<S>, action: Action, next: Dispatcher<Action>) {
            if (action is A) {
                block(store, action, next)
            } else {
                next(action)
            }
        }
    }
}

inline fun <S, reified A : Action> createMiddleware(crossinline block: suspend (getState: () -> S, action: A, next: Dispatcher<A>) -> Unit): Middleware<S> {
    return middleware<S, A> { store, action, next -> block(store.state::value, action, next) }
}