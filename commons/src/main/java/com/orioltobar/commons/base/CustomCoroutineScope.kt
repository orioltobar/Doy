package com.orioltobar.commons.base

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

private val defaultExceptionHandler = CoroutineExceptionHandler { _, throwable -> throw throwable }

class CustomCoroutineScope(
    job: Job = SupervisorJob(),
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
    exceptionHandler: CoroutineExceptionHandler = defaultExceptionHandler
) : CoroutineScope {

    override val coroutineContext: CoroutineContext = job + dispatcher + exceptionHandler
}