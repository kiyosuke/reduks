package com.kiyosuke.reduks

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Store
 *
 * Example: In Android ViewModel
 * ```kotlin
 * class SampleViewModel : ViewModel() {
 *     val store = Store<SampleState, SampleAction>(
 *         initialState = SampleState(),
 *         coroutineScope = viewModelScope,
 *         reducer = SampleReducer()
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
class Store<S, A>(
    initialState: S,
    private val coroutineScope: CoroutineScope,
    reducer: Reducer<S, A>,
    middlewares: List<Middleware<S, A>> = emptyList()
) {
    // Actions flow
    private val actions = MutableSharedFlow<A>()

    private val _state = MutableStateFlow(initialState)
    val state = _state.asStateFlow()

    // Middlewareを考慮した上で最初に実行するDispatcher
    private val entranceDispatcher: Dispatcher<A>

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
    private fun setupMiddlewares(middlewares: List<Middleware<S, A>>): Dispatcher<A> {
        val dispatchers = mutableListOf<Dispatcher<A>>(::dispatchTo)
        for (middleware in middlewares) {
            val next = dispatchers.last()
            val dispatcher: Dispatcher<A> = { action ->
                middleware.apply(this, action, next)
            }
            dispatchers.add(dispatcher)
        }
        return dispatchers.reversed().first()
    }

    private suspend fun dispatchTo(action: A) {
        actions.emit(action)
    }

    fun dispatch(action: A) {
        coroutineScope.launch {
            entranceDispatcher(action)
        }
    }
}