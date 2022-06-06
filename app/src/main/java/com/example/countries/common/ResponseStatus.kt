package com.example.countries.common

import com.example.countries.model.Countries

sealed interface ResponseStatus {
    class LOADING(val isLoading: Boolean = true) : ResponseStatus
    class SUCCESS(val countries: List<Countries>) : ResponseStatus
    class ERROR(val error: Throwable) : ResponseStatus
}