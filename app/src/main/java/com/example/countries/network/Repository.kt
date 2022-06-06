package com.example.countries.network

import com.example.countries.common.NullResponseException
import com.example.countries.common.ResponseIsAFailure
import com.example.countries.common.ResponseStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface CountriesRepository {
    fun getAllCountries(): Flow<ResponseStatus>
}

class CountriesRepositoryImpl @Inject constructor(
    private val interfaceService: InterfaceService
) : CountriesRepository {

    override fun getAllCountries(): Flow<ResponseStatus> =
        flow {
            emit(ResponseStatus.LOADING())

            try {
                val response = interfaceService.getCountries()
                if (response.isSuccessful) {
                    response.body()?.let {
                        emit(ResponseStatus.SUCCESS(it))
                    } ?: throw NullResponseException()
                } else {
                    throw ResponseIsAFailure()
                }
            } catch (e: Exception) {
                emit(ResponseStatus.ERROR(e))
            }
        }
}