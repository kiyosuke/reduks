package com.kiyosuke.reduks

interface Reducer<S, A> {

    fun reduce(action: A, currentState: S): S
}

inline fun <S, A> reducer(crossinline block: (action: A, currentState: S) -> S): Reducer<S, A> {
    return object : Reducer<S, A> {
        override fun reduce(action: A, currentState: S): S = block(action, currentState)
    }
}