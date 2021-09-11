package com.kiyosuke.counter

import com.github.kiyosuke.reduks.Action

sealed class CounterAction : Action {

    object Increment : CounterAction() {
        override fun toString(): String {
            return "CounterAction.Increment"
        }
    }

    object Decrement : CounterAction() {
        override fun toString(): String {
            return "CounterAction.Decrement"
        }
    }

    object Reset : CounterAction() {
        override fun toString(): String {
            return "CounterAction.Reset"
        }
    }
}