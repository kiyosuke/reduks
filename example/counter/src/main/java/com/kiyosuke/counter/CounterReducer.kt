package com.kiyosuke.counter

import com.github.kiyosuke.reduks.typedReducer

val counterReducer = typedReducer<CounterState, CounterAction> { action, state ->
    when (action) {
        CounterAction.Increment -> state.copy(count = state.count + 1)
        CounterAction.Decrement -> if (state.count > 0) {
            state.copy(count = state.count - 1)
        } else {
            state
        }
        CounterAction.Reset -> CounterState()
    }
}