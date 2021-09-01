package com.kiyosuke.counter

sealed class CounterAction {

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