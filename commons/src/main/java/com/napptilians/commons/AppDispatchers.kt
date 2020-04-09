package com.napptilians.commons

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Defines the dispatchers available to use to handle the thread pools of the coroutines.
 *
 * @property main Main dispatcher that uses the UI thread of Android.
 * @property io uses a shared pool of on-demand created threads and is designed for
 *                        offloading of IO-intensive blocking operations.
 * @property default This dispatcher is used to run tasks that make intensive use of the CPU
 *                        (App computations, algorithms, etc). It can use as many threads as
 *                        CPU cores.
 * @property unconfined This dispatcher starts a coroutine in the caller thread, but only until
 *                        the first suspension point. After suspension the coroutine resumes in
 *                        whatever thread that is used by the corresponding suspending function,
 *                        without confining it to any specific thread or pool. SHOULD NOT BE USED
 *                        IN PRODUCTION CODE.
 */
interface AppDispatchers {

    val main: CoroutineDispatcher

    val io: CoroutineDispatcher

    val default: CoroutineDispatcher

    val unconfined: CoroutineDispatcher
}
