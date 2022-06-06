package com.example.countries.network

import com.example.countries.model.Countries
import retrofit2.Response
import retrofit2.http.GET

interface InterfaceService {

    @GET(COUNTRIES_PATH)
    suspend fun getCountries(): Response<List<Countries>>

    companion object {
        const val BASE_URL = "https://gist.githubusercontent.com/peymano-wmt/32dcb892b06648910ddd40406e37fdab/raw/db25946fd77c5873b0303b858e861ce724e0dcd0/"
          private const val COUNTRIES_PATH = "countries.json"
    }
}