package com.kiyosuke.counter

import com.kiyosuke.reduks.Reducer
import com.kiyosuke.reduks.reducer

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