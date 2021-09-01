package com.kiyosuke.counter

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.kiyosuke.counter.databinding.ActivityCounterBinding
import kotlinx.coroutines.flow.collect

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
        binding.buttonReset.setOnClickListener { viewModel.onResetClicked() }
    }
}