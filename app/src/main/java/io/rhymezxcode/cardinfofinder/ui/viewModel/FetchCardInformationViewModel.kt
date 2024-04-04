package io.rhymezxcode.cardinfofinder.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rhymezxcode.cardinfofinder.data.models.CardInfoPage
import io.rhymezxcode.cardinfofinder.data.repository.FetchCardInformationRepository
import io.rhymezxcode.cardinfofinder.util.Constants
import io.rhymezxcode.cardinfofinder.util.Event
import io.rhymezxcode.cardinfofinder.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class FetchCardInformationViewModel @Inject constructor(
    private val repo: FetchCardInformationRepository
) : ViewModel() {

    private val _getResponse =
        MutableStateFlow<Event<Resource<CardInfoPage>>>(Event(Resource.Empty()))
    val getResponse: StateFlow<Event<Resource<CardInfoPage>>> =
        _getResponse

    fun fetchInfoNow(
        cardNumber: String
    ) = viewModelScope.launch {
        fetchCardInfo(cardNumber)
    }

    private suspend fun fetchCardInfo(
        cardNumber: String
    ) {
        _getResponse.value = Event(Resource.Loading())
        try {
            val response = repo.fetchCardInformation(cardNumber)
            _getResponse.value = handleResponse(response)
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _getResponse.value = Event(Resource.Error(Constants.NETWORK_FAILURE))
                }

                else -> {
                    _getResponse.value = Event(Resource.Error(t.localizedMessage ?: ""))
                }
            }
        }
    }

    private fun handleResponse(response: Response<CardInfoPage>): Event<Resource<CardInfoPage>> {
        return if (response.isSuccessful) {
            val data = response.body()
            if (data != null) {
                Event(Resource.Success(data))
            } else {
                Event(Resource.Error(Constants.NULL_RESPONSE))
            }
        } else {
            Event(Resource.Error(Constants.SOMETHING_IS_WRONG))
        }
    }
}