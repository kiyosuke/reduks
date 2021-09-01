package com.kiyosuke.todo

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.kiyosuke.todo.databinding.ActivityTodoBinding
import com.kiyosuke.todo.databinding.TodoItemBinding
import com.kiyosuke.todo.model.Todo
import kotlinx.coroutines.flow.collect

class TodoActivity : AppCompatActivity() {

    private val viewModel: TodoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = TodoAdapter()
        adapter.onItemClickListener = { todo -> viewModel.onToggleTodo(todo) }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { state ->
                adapter.items = state.filtered
            }
        }

        binding.editText.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.onAddTodo(textView.text.toString())
                textView.text = ""
                return@setOnEditorActionListener true
            }
            false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_todo_filter, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.filter_all -> {
                viewModel.onChangeFilter(VisibilityFilter.SHOW_ALL)
                true
            }
            R.id.filter_completed -> {
                viewModel.onChangeFilter(VisibilityFilter.SHOW_COMPLETE)
                true
            }
            R.id.filter_active -> {
                viewModel.onChangeFilter(VisibilityFilter.SHOW_ACTIVE)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    class TodoAdapter : RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

        var items: List<Todo> = emptyList()
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        var onItemClickListener: ((Todo) -> Unit)? = null

        override fun getItemCount(): Int = items.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = TodoItemBinding.inflate(inflater, parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            holder.binding.textTask.text = item.text
            val color = if (item.isActive) Color.WHITE else Color.LTGRAY
            holder.binding.root.setBackgroundColor(color)
            holder.binding.root.setOnClickListener { onItemClickListener?.invoke(item) }
        }

        class ViewHolder(val binding: TodoItemBinding) : RecyclerView.ViewHolder(binding.root)
    }
}