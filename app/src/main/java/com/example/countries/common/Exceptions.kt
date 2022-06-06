package com.example.countries.common

class NullResponseException(message: String = "Response is null") : Exception(message)
class ResponseIsAFailure(message: String = "Error: failure in the response") : Exception(message)