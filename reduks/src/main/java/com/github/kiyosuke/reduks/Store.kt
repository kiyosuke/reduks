package com.github.kiyosuke.reduks

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Store
 *
 * Example: In Android ViewModel
 * ```kotlin
 * class SampleViewModel : ViewModel() {
 *     val store = Store<SampleState>(
 *         initialState = SampleState(),
 *         coroutineScope = viewModelScope,
 *         reducer = sampleReducer
 *     )
 *
 *     val state = store.state
 * }
 * ```
 *
 * @param initialState Initial State
 * @param coroutineScope to launch actions
 * @param reducer Reducer
 * @param middlewares Middlewares
 */
class Store<S>(
    initialState: S,
    private val coroutineScope: CoroutineScope,
    reducer: Reducer<S, Action>,
    middlewares: List<Middleware<S>> = emptyList()
) {
    // Actions flow
    private val actions = MutableSharedFlow<Action>()

    private val _state = MutableStateFlow(initialState)
    val state = _state.asStateFlow()

    // Middlewareを考慮した上で最初に実行するDispatcher
    private val entranceDispatcher: Dispatcher<Action>

    init {
        entranceDispatcher = setupMiddlewares(middlewares)

        actions.map { action -> reducer.reduce(action, state.value) }
            .distinctUntilChanged()
            .onEach { state -> _state.emit(state) }
            .launchIn(coroutineScope)
    }

    /**
     * Middlewareの実行とStore#dispatchの実行を考慮した最初に実行するDispatchオブジェクトを生成
     */
    private fun setupMiddlewares(middlewares: List<Middleware<S>>): Dispatcher<Action> {
        val dispatchers = mutableListOf<Dispatcher<Action>>(::dispatchTo)
        for (middleware in middlewares) {
            val next = dispatchers.last()
            val dispatcher: Dispatcher<Action> = { action ->
                middleware.apply(this, action, next)
            }
            dispatchers.add(dispatcher)
        }
        return dispatchers.reversed().first()
    }

    private suspend fun dispatchTo(action: Action) {
        actions.emit(action)
    }

    fun dispatch(action: Action) {
        coroutineScope.launch {
            entranceDispatcher(action)
        }
    }
}