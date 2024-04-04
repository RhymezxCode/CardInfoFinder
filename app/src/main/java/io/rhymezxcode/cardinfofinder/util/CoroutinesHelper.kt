package com.nibbssdk.util

import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object CoroutinesHelper {
    suspend fun <T> withIO(block: suspend CoroutineScope.() -> T): T {
        return withContext(Dispatchers.IO, block)
    }

    fun coIO(runner: suspend CoroutineScope.() -> Unit) = CoroutineScope(Dispatchers.IO).launch { runner.invoke((this)) }

    fun coMain(runner: suspend CoroutineScope.() -> Unit) = CoroutineScope(Dispatchers.Main).launch { runner.invoke((this)) }

    suspend fun <A, B> Iterable<A>.pmap(f: suspend (A) -> B): List<B> =
        coroutineScope {
            map { async { f(it) } }.awaitAll()
        }

    suspend fun <T> coAsync(block: suspend () -> T): T = coroutineScope {
        val deferred = async { block.invoke() }
        deferred.await()
    }

    suspend fun onBackgroundReturnable(block: suspend () -> Int?) : Int? {
        return withContext(Dispatchers.Default) {
            block()
        }
    }

    suspend fun <T> onBackground(block: suspend () -> T): T {
        return withContext(Dispatchers.Default) {
            block()
        }
    }
    
    
    suspend fun <T> LifecycleCoroutineScope.toBackground(block: suspend () -> T) {
        async(Dispatchers.IO) {
            block()
        }.await()
    }
    
}
