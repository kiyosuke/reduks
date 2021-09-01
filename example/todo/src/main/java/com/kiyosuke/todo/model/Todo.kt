package com.kiyosuke.todo.model

data class Todo(
    val id: Int,
    val text: String,
    val completed: Boolean = false
) {
    val isActive: Boolean get() = !completed
}