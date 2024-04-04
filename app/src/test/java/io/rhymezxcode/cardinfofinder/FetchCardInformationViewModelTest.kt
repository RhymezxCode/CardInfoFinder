package io.rhymezxcode.cardinfofinder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.mockk
import io.rhymezxcode.cardinfofinder.data.models.CardInfoPage
import io.rhymezxcode.cardinfofinder.data.repository.FetchCardInformationRepository
import io.rhymezxcode.cardinfofinder.ui.viewModel.FetchCardInformationViewModel
import io.rhymezxcode.cardinfofinder.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class FetchCardInformationViewModelTest {

    private lateinit var viewModel: FetchCardInformationViewModel
    private val mockRepository: FetchCardInformationRepository = mockk()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        viewModel = FetchCardInformationViewModel(mockRepository)
    }

    @Test
    fun `fetchInfoNow() emits loading state followed by success state on successful response`() =
        runBlockingTest {
            // Mock successful response
            val mockResponse: Response<CardInfoPage> = mockk()
            val mockCardInfoPage: CardInfoPage = mockk()
            coEvery { mockRepository.fetchCardInformation(any()) } returns mockResponse
            coEvery { mockResponse.isSuccessful } returns true
            coEvery { mockResponse.body() } returns mockCardInfoPage

            // Execute the ViewModel action
            viewModel.fetchInfoNow("1234567890")

            // Assert loading state emitted
            assertEquals(
                Resource.Loading<Any>().message,
                viewModel.getResponse.first().peekContent().message
            )

            // Assert success state emitted after loading
            val successEvent = viewModel.getResponse.first()
            assertTrue(successEvent.peekContent() is Resource.Success)
            assertEquals(mockCardInfoPage, (successEvent.peekContent() as Resource.Success).data)
        }


    //TODO: Add more test cases
}
