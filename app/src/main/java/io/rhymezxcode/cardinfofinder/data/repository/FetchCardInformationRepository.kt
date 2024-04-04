package io.rhymezxcode.cardinfofinder.data.repository

import io.rhymezxcode.cardinfofinder.data.models.CardInfoPage
import retrofit2.Response

interface FetchCardInformationRepository {

    suspend fun fetchCardInformation(
        cardNumber: String?
    ): Response<CardInfoPage>

}