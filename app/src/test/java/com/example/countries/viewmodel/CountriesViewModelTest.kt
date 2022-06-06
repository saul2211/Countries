package com.example.countries.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.countries.network.CountriesRepository
import com.example.countries.common.ResponseStatus
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CountriesViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    private val mockRepository = mockk<CountriesRepository>(relaxed = true)

    private lateinit var targetTest: CountryViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        targetTest = CountryViewModel(mockRepository, testDispatcher)
    }

    @After
    fun shutdown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    @Test
    fun `get countries when the server retrieves a list of countries it should return success`() {
        every { mockRepository.getAllCountries() } returns flowOf(
            ResponseStatus.SUCCESS(listOf(mockk()))
        )
        val stateList = mutableListOf<ResponseStatus>()
        targetTest.countries.observeForever {
            stateList.add(it)
        }

        targetTest.getAllCountries()

        assertThat(stateList).hasSize(2)
        assertThat(stateList[0]).isInstanceOf(ResponseStatus.LOADING::class.java)
        assertThat(stateList[1]).isInstanceOf(ResponseStatus.SUCCESS::class.java)

        assertThat((stateList[1] as ResponseStatus.SUCCESS).countries).hasSize(1)
    }

    @Test
    fun `get countries when the server retrieves a list of countries it should return error`() {
        every { mockRepository.getAllCountries() } returns flowOf(
            ResponseStatus.ERROR(Throwable("Error"))
        )
        val stateList = mutableListOf<ResponseStatus>()
        targetTest.countries.observeForever {
            stateList.add(it)
        }

        targetTest.getAllCountries()

        assertThat(stateList).hasSize(2)
        assertThat(stateList[0]).isInstanceOf(ResponseStatus.LOADING::class.java)
        assertThat(stateList[1]).isInstanceOf(ResponseStatus.ERROR::class.java)

        assertThat((stateList[1] as ResponseStatus.ERROR).error.localizedMessage).isEqualTo("Error")
    }

    @Test
    fun `get countries when the server retrieves a list of countries it should return loading`() {
        every { mockRepository.getAllCountries() } returns flowOf(
            ResponseStatus.LOADING()
        )
        val stateList = mutableListOf<ResponseStatus>()
        targetTest.countries.observeForever {
            stateList.add(it)
        }

        targetTest.getAllCountries()

        assertThat(stateList).hasSize(2)
        assertThat(stateList[0]).isInstanceOf(ResponseStatus.LOADING::class.java)
        assertThat(stateList[1]).isInstanceOf(ResponseStatus.LOADING::class.java)

        assertThat((stateList[1] as ResponseStatus.LOADING).isLoading).isTrue()
    }
}