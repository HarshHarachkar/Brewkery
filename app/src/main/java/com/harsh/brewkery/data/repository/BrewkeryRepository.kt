package com.harsh.brewkery.data.repository

import com.harsh.brewkery.data.remote.RetrofitClient

class BrewkeryRepository {

    private val api = RetrofitClient.apiService

    suspend fun getMenu() = api.getMenu()

    suspend fun getItemDetails(itemId: Int) =
        api.getItemDetails(itemId)
}