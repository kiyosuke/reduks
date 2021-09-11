package com.github.kiyosuke.reduks

interface Reducer<S, in A : Action> {

    fun reduce(action: A, currentState: S): S
}

inline fun <S> reducer(crossinline block: (action: Action, currentState: S) -> S): Reducer<S, Action> {
    return object : Reducer<S, Action> {
        override fun reduce(action: Action, currentState: S): S = block(action, currentState)
    }
}

inline fun <S, reified A : Action> typedReducer(crossinline block: (action: A, currentState: S) -> S): Reducer<S, Action> {
    return object : Reducer<S, Action> {
        override fun reduce(action: Action, currentState: S): S {
            if (action is A) {
                return block(action, currentState)
            }
            return currentState
        }
    }
}