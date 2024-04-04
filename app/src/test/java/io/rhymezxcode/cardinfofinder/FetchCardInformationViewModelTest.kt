package io.rhymezxcode.cardinfofinder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.rhymezxcode.cardinfofinder.data.models.CardInfoPage
import io.rhymezxcode.cardinfofinder.data.repository.FetchCardInformationRepository
import io.rhymezxcode.cardinfofinder.ui.viewModel.FetchCardInformationViewModel
import io.rhymezxcode.cardinfofinder.util.Resource
import io.rhymezxcode.cardinfofinder.util.Resource.Loading
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class FetchCardInformationViewModelTest {

    // This rule swaps the background executor used by the Architecture Components with a different one which executes each task synchronously.
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Use a TestCoroutineDispatcher to manually control execution of coroutines
    private val testDispatcher = TestCoroutineDispatcher()

    // Use a TestCoroutineScope to easily control coroutine execution
    private val testScope = TestCoroutineScope(testDispatcher)

    // The ViewModel under test
    private lateinit var viewModel: FetchCardInformationViewModel

    // Mock repository
    private val mockRepository: FetchCardInformationRepository = mockk()

    @Before
    fun setup() {
        // Create the ViewModel with the TestCoroutineDispatcher
        viewModel = FetchCardInformationViewModel(mockRepository)
    }

    @Test
    fun `fetchInfoNow() should emit loading state followed by success state on successful response`() =
        testScope.runBlockingTest {
            // Mock successful response
            val mockResponse: Response<CardInfoPage> = mockk()
            val mockCardInfoPage: CardInfoPage = mockk()
            coEvery { mockRepository.fetchCardInformation(any()) } returns mockResponse
            coEvery { mockResponse.isSuccessful } returns true
            coEvery { mockResponse.body() } returns mockCardInfoPage

            // Call the method to be tested
            viewModel.fetchInfoNow("1234567890")

            // Verify loading state emitted
            val loadingEvent = viewModel.getResponse.value
            assert(loadingEvent.peekContent() is Loading)

            // Advance coroutine execution
            testDispatcher.scheduler.advanceUntilIdle()

            // Verify success state emitted
            val successEvent = viewModel.getResponse.value
            assert(successEvent.peekContent() is Resource.Success)

            // Verify the repository method was called with the correct parameters
            coVerify { mockRepository.fetchCardInformation("1234567890") }
        }

    // Similar tests can be written for error cases and other scenarios
}
