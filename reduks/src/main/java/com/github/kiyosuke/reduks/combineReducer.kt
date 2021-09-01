package com.github.kiyosuke.reduks

fun <S, A> combineReducers(vararg reducers: Reducer<S, A>): Reducer<S, A> {
    return reducer { action, currentState ->
        var state = currentState
        for (reducer in reducers) {
            state = reducer.reduce(action, state)
        }
        state
    }
}