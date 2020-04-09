package com.napptilians.commons.base

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob

private val defaultExceptionHandler = CoroutineExceptionHandler { _, throwable -> throw throwable }

class CustomCoroutineScope(
    job: Job = SupervisorJob(),
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
    exceptionHandler: CoroutineExceptionHandler = defaultExceptionHandler
) : CoroutineScope {

    override val coroutineContext: CoroutineContext = job + dispatcher + exceptionHandler
}
