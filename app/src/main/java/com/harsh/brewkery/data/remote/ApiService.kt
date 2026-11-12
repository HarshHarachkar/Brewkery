package com.harsh.brewkery.data.remote

import com.harsh.brewkery.data.model.MenuResponse
import com.harsh.brewkery.data.model.ItemDetails
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("VivekShah138/Brewkery/main/data.json")
    suspend fun getMenu(): MenuResponse

    @GET("VivekShah138/Brewkery/main/api/items/{id}.json")
    suspend fun getItemDetails(
        @Path("id") itemId: Int
    ): ItemDetails
}