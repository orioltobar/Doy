package com.napptilians.features.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.napptilians.commons.Success
import com.napptilians.domain.usecases.GetMovieUseCase
import com.napptilians.domain.usecases.GetMovieUseCaseFlow
import com.napptilians.features.CoroutineTestRule
import com.napptilians.features.NewValue
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class MovieViewModelTest {

    init {
        MockKAnnotations.init(this, relaxed = true)
    }

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var rule: TestRule = CoroutineTestRule()

    @MockK
    private lateinit var getMovieUseCase: GetMovieUseCase

    @MockK
    private lateinit var getMovieUseCaseFlow: GetMovieUseCaseFlow

    private val movieViewModel: MovieViewModel by lazy {
        MovieViewModel(getMovieUseCase, getMovieUseCaseFlow)
    }

    @Before
    fun setUp() {
        coEvery { getMovieUseCase.execute(any()) } returns Success(mockk())
        every { getMovieUseCaseFlow.execute() } returns mockk()
    }

    @Test
    fun `viewModel emits the value when useCase returns the result`() = runBlockingTest {
        movieViewModel.execute(15)

        Assert.assertTrue(movieViewModel.movieDataStream.value is NewValue)
    }
}