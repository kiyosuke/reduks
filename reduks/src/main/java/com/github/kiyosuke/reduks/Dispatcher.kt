package com.github.kiyosuke.reduks

typealias Dispatcher<A> = suspend (action: A) -> Unit