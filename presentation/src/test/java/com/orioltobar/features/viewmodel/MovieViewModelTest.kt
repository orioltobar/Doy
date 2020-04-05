package com.orioltobar.features.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.orioltobar.commons.Success
import com.orioltobar.domain.usecases.GetMovieUseCase
import com.orioltobar.domain.usecases.GetMovieUseCaseFlow
import com.orioltobar.features.CoroutineTestRule
import com.orioltobar.features.NewValue
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