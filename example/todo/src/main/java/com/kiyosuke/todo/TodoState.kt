package com.kiyosuke.todo

import com.kiyosuke.todo.model.Todo

data class TodoState(
    val todos: List<Todo> = emptyList(),
    val filter: VisibilityFilter = VisibilityFilter.SHOW_ALL
) {
    val filtered: List<Todo> = when (filter) {
        VisibilityFilter.SHOW_ALL -> todos
        VisibilityFilter.SHOW_COMPLETE -> todos.filter { it.completed }
        VisibilityFilter.SHOW_ACTIVE -> todos.filter { it.isActive }
    }
}