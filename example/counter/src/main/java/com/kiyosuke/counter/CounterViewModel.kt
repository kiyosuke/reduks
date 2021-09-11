package com.kiyosuke.counter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kiyosuke.reduks.Store
import com.kiyosuke.common.logger

class CounterViewModel : ViewModel() {

    private val store: Store<CounterState> = Store(
        initialState = CounterState(),
        coroutineScope = viewModelScope,
        reducer = counterReducer,
        middlewares = listOf(
            logger(),
        )
    )

    init {
        store.dispatch(CounterAction.Increment)
        store.dispatch(CounterAction.Increment)
        store.dispatch(CounterAction.Decrement)
    }

    val state = store.state

    fun onIncrementClicked() {
        store.dispatch(CounterAction.Increment)
    }

    fun onDecrementClicked() {
        store.dispatch(CounterAction.Decrement)
    }

    fun onResetClicked() {
        store.dispatch(CounterAction.Reset)
    }
}
