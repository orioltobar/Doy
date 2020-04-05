package com.orioltobar.features

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Coroutine test rule to allow the usage of Main thread in test environment.
 * Link: https://gist.github.com/AniketSK/0fd48da9ed969eee307f92457115612a
 */
@ExperimentalCoroutinesApi
class CoroutineTestRule(private val dispatcher: CoroutineDispatcher = TestCoroutineDispatcher()) :
    TestWatcher() {

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}