package io.rhymezxcode.cardinfofinder.data.repository

import io.rhymezxcode.cardinfofinder.data.providers.remote.CardInfoFinderApiList
import javax.inject.Inject

class FetchCardInformationRepositoryImpl @Inject constructor(
    private val api: CardInfoFinderApiList
) : FetchCardInformationRepository {
    override suspend fun fetchCardInformation(
        cardNumber: String?
    ) = api.fetchCardInformation(cardNumber ?: "")

}