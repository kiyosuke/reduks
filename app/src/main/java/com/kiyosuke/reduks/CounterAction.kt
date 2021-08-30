package com.kiyosuke.reduks

sealed class CounterAction {

    object Increment : CounterAction()

    object Decrement : CounterAction()
}