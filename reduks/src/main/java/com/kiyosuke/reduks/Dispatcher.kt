package com.kiyosuke.reduks

typealias Dispatcher<A> = suspend (action: A) -> Unit