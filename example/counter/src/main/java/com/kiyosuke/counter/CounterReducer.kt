package com.kiyosuke.counter

import com.github.kiyosuke.reduks.Reducer

class CounterReducer : Reducer<CounterState, CounterAction> {
    override fun reduce(action: CounterAction, currentState: CounterState): CounterState {
        return when (action) {
            CounterAction.Increment -> currentState.copy(count = currentState.count + 1)
            CounterAction.Decrement -> if (currentState.count > 0) {
                currentState.copy(count = currentState.count - 1)
            } else {
                currentState
            }
            CounterAction.Reset -> CounterState()
        }
    }
}