package com.kiyosuke.todo

private var nextTodoId = 0

sealed class TodoAction {
    data class AddTodo(
        val id: Int = ++nextTodoId,
        val text: String
    ) : TodoAction()

    data class ToggleTodo(val id: Int) : TodoAction()

    data class SetVisibilityFilter(val filter: VisibilityFilter) : TodoAction()
}
