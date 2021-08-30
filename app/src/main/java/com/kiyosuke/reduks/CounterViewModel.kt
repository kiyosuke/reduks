package com.kiyosuke.reduks

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

private const val TAG = "CounterViewModel"
private val logMiddleware = middleware<CounterState, CounterAction> { store, action, next ->
    Log.d(TAG, "action: [$action], state: ${store.state.value}")
    next(action)
}

class CounterViewModel : ViewModel() {

    private val store: Store<CounterState, CounterAction> = Store(
        initialState = CounterState(),
        coroutineScope = viewModelScope,
        reducer = CounterReducer(),
        middlewares = listOf(
            logMiddleware,
        )
    )

    val state = store.state

    fun onIncrementClicked() {
        store.dispatch(CounterAction.Increment)
    }

    fun onDecrementClicked() {
        store.dispatch(CounterAction.Decrement)
    }
}
