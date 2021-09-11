# reduks
Redux for Android by Kotlin Coroutines Flow

## dependencies
- kotlin-coroutines-core: 1.5.0

## Usage
```kotlin
// Create a state
// State is immutable
data class CounterState(val count: Int = 0)

// Create actions
// It is recommended to define it with sealed class or sealed interface
sealed class CounterAction : Action {
    object Increment : CounterAction()
    object Decrement : CounterAction()
}

// Create a reducer
// A Reducer is pure function
val counterReducer = typedReducer<CounterState, CounterAction> {
    when (action) {
        CounterAction.Increment -> currentState.copy(count = currentState.count + 1)
        CounterAction.Decrement -> currentState.copy(count = currentState.count - 1)
    }
}

private const val TAG = "CounterViewModel"
private val logMiddleware = Middleware<CounterState> { store, action, next ->
    Log.d(TAG, "action: [$action], state: ${store.state.value}")
    next(action)
}

// Create a ViewModel
class CounterViewModel : ViewModel() {
    // Define and use one Store in one ViewModel
    private val store: Store<CounterState> = Store(
        initialState = CounterState(),
        coroutineScope = viewModelScope,
        reducer = counterReducer,
        middlewares = listOf(
            logMiddleware,
        )
    )
    
    // Publish the State of the Store as it is
    val state = store.state

    // Handle UI events
    fun onIncrementClicked() {
        store.dispatch(CounterAction.Increment)
    }

    fun onDecrementClicked() {
        store.dispatch(CounterAction.Decrement)
    }
}

class CounterActivity : AppCompatActivity() {

    private val viewModel: CounterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityCounterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { state ->
                binding.textCount.text = state.count.toString()
            }
        }

        binding.fabIncrement.setOnClickListener { viewModel.onIncrementClicked() }
        binding.fabDecrement.setOnClickListener { viewModel.onDecrementClicked() }
    }
}
```

## Examples
- [Counter](/example/counter)
- [ToDo](/example/todo)
