package com.kiyosuke.todo

import com.github.kiyosuke.reduks.typedReducer
import com.kiyosuke.todo.model.Todo

val todoReducer = typedReducer<TodoState, TodoAction> { action, currentState ->
    when (action) {
        is TodoAction.AddTodo -> {
            val todo = Todo(action.id, action.text)
            currentState.copy(todos = currentState.todos + todo)
        }
        is TodoAction.SetVisibilityFilter -> currentState.copy(filter = action.filter)
        is TodoAction.ToggleTodo -> {
            val index = currentState.todos.indexOfFirst { it.id == action.id }
            if (index != -1) {
                val todos = currentState.todos.toMutableList()
                val todo = todos[index]
                val newTodo = todo.copy(completed = !todo.completed)
                todos[index] = newTodo
                currentState.copy(todos = todos)
            } else {
                currentState
            }
        }
    }
}