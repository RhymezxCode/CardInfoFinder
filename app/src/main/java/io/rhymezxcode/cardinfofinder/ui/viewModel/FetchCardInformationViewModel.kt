package io.rhymezxcode.cardinfofinder.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rhymezxcode.cardinfofinder.data.models.CardInfoPage
import io.rhymezxcode.cardinfofinder.data.repository.FetchCardInformationRepository
import io.rhymezxcode.cardinfofinder.util.Event
import io.rhymezxcode.cardinfofinder.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class FetchCardInformationViewModel @Inject constructor(
    private val repo: FetchCardInformationRepository
) : ViewModel() {

    private val _getResponse =
        MutableLiveData<Event<Resource<CardInfoPage>>>()
    val getResponse: LiveData<Event<Resource<CardInfoPage>>> =
        _getResponse


    fun fetchInfoNow(
        cardNumber: String
    ) = viewModelScope.launch {
        fetchCardInfo(cardNumber)
    }

    private suspend fun fetchCardInfo(
        cardNumber: String
    ) {
        _getResponse.postValue(Event(Resource.Loading()))
        try {

            val response = repo.fetchCardInformation(cardNumber)
            _getResponse.postValue(handleResponse(response))

        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _getResponse.postValue(
                        Event(
                            Resource.Error(
                                "Network Failure"
                            )
                        )
                    )
                }

                else -> {
                    _getResponse.postValue(
                        Event(
                            Resource.Error(
                                t.localizedMessage ?: ""
                            )
                        )
                    )
                }
            }
        }
    }

    private fun handleResponse(response: Response<CardInfoPage>):
            Event<Resource<CardInfoPage>> {
        if (response.isSuccessful) {
            Resource.Success(response)
        }
        return when {
            else -> Event(Resource.Error("Something went wrong!"))
        }


    }
}