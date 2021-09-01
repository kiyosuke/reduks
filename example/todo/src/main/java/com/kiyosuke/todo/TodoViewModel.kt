package com.kiyosuke.todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiyosuke.reduks.Middleware
import com.kiyosuke.reduks.Store
import com.kiyosuke.reduks.middlewares.createMiddleware
import com.kiyosuke.todo.model.Todo

typealias TodoStore = Store<TodoState, TodoAction>

private val inputValidate: Middleware<TodoState, TodoAction> = createMiddleware { _, action, next ->
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