package com.github.kiyosuke.reduks

fun <S> combineReducers(vararg reducers: Reducer<S, Action>): Reducer<S, Action> {
    return reducer { action, currentState ->
        var state = currentState
        for (reducer in reducers) {
            state = reducer.reduce(action, state)
        }
        state
    }
}