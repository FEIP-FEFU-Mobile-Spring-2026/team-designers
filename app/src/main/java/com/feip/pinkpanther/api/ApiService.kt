package com.feip.pinkpanther.data.api

import com.feip.pinkpanther.models.ProductsData
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {
    @GET("catalog")
    suspend fun getCatalog(
        @Header("Authorization") token: String
    ): ProductsData
}

