package com.kiyosuke.todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kiyosuke.reduks.Middleware
import com.github.kiyosuke.reduks.Store
import com.github.kiyosuke.reduks.middlewares.createMiddleware
import com.kiyosuke.common.logger
import com.kiyosuke.todo.model.Todo

typealias TodoStore = Store<TodoState>

private val inputValidate: Middleware<TodoState> = createMiddleware<TodoState, TodoAction> { _, action, next ->
    if (action is TodoAction.AddTodo) {
        if (action.text.isNotEmpty()) {
            next(action)
        }
    } else {
        next(action)
    }
}

class TodoViewModel : ViewModel() {
    private val store = TodoStore(
        initialState = TodoState(),
        coroutineScope = viewModelScope,
        reducer = todoReducer,
        middlewares = listOf(
            inputValidate,
            logger()
        )
    )

    val state = store.state

    fun onAddTodo(text: String) {
        store.dispatch(TodoAction.AddTodo(text = text))
    }

    fun onToggleTodo(todo: Todo) {
        store.dispatch(TodoAction.ToggleTodo(todo.id))
    }

    fun onChangeFilter(filter: VisibilityFilter) {
        store.dispatch(TodoAction.SetVisibilityFilter(filter))
    }
}