package com.kiyosuke.counter

import android.util.Log
import com.kiyosuke.reduks.Middleware
import com.kiyosuke.reduks.middlewares.createMiddleware

fun <S, A> logger(tag: String): Middleware<S, A> = createMiddleware { getState, action, next ->
    Log.d(tag, "action: [$action], before: ${getState()}")
    next(action)
    Log.d(tag, "action: [$action], after: ${getState()}")
}