package com.example.skysnapproject.favFeatsure

import com.example.skysnapproject.dataLayer.models.Place
import com.example.skysnapproject.dataLayer.repo.RepositoryInterface
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class FavViewModelTest {

    private lateinit var repository: RepositoryInterface
    private lateinit var favViewModel: FavViewModel


    private val favPlaces = listOf(
        Place(id = 1, lat = 123.123, lng = 12.021),
        Place(id = 20, lat = 877.123, lng = 132.44),
    )
    private val testPlace = Place(id = 3, lat = 10.0, lng = 20.0)


    @Before
    fun setup() = runTest {
        repository = mockk()

        Dispatchers.setMain(StandardTestDispatcher())

        coEvery { repository.getFavPlace() } returns flowOf(favPlaces)
        coEvery { repository.addPlace(testPlace) } returns 1

        favViewModel = FavViewModel(repository)

    }


    @After
    fun tearDown() = Dispatchers.resetMain()


    @Test
    fun loadFavorites_retunallplces() = runTest {

        //when
        val ans = favViewModel .favPlaces.value
        advanceUntilIdle()


        //then
        assertEquals(favPlaces, ans)
    }


    @Test
    fun saveLocation_addsPlaceSuccessfully() = runTest {
        //when
        favViewModel.saveLocation(testPlace)

        advanceUntilIdle()

        // then
        coVerify { repository.addPlace(testPlace) }
    }

}