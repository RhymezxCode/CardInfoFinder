package io.rhymezxcode.cardinfofinder.data.providers.remote

import io.rhymezxcode.cardinfofinder.data.models.CardInfoPage
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CardInfoFinderApiList {

    @GET("/{cardNumber}")
    suspend fun fetchCardInformation(
        @Path("cardNumber") cardNumber: String
    ): Response<CardInfoPage> // response data
}

