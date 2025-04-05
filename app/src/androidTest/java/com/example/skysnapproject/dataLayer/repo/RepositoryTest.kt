package com.example.skysnapproject.dataLayer.repo

import com.example.skysnapproject.dataLayer.models.Place
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test



class RepositoryTest {

    private lateinit var fakeLocalDataSource: FakeLocalDataSource
    private lateinit var fakeRemoteDataSource: FakeRemoteDataSource
    private lateinit var repository: Repository


    @Before
    fun setup() {

        fakeLocalDataSource = FakeLocalDataSource()
        fakeRemoteDataSource = FakeRemoteDataSource()
        repository = Repository(fakeRemoteDataSource, fakeLocalDataSource)

        runBlocking {

            fakeLocalDataSource.insert(Place(1, 10.23, 20.11, "Cairo"))
            fakeLocalDataSource.insert(Place(2, 4.4, 75.11, "Giza"))
        }

    }

    @Test
    fun getFavPlace_returnAllPlaces() = runTest {

        //when
        val result = repository.getFavPlace().first()

        //then
        assertEquals(2, result.size)
        assertEquals("Cairo", result[0].name)
        assertThat(result[1].lat, `is`(4.4))
    }

    @Test
    fun removePlace_removeOne_returnOnePlace() = runTest {

        //when
        repository.removePlace(Place(1, 10.23, 20.11, "Cairo"))

        val result = repository.getFavPlace().first()
        //then
        assertEquals(1, result.size)
        assertEquals("Giza", result[0].name)

    }

    @Test
    fun searchLocation_returnMockLocations() = runTest {

        // when
        val result = repository.searchLocation("Cairo").first()

        // then
        assertEquals(2, result.size)
        assertEquals("Cairo, Egypt", result[0].display_name)
        assertEquals("456.456", result[1].lat)
    }


}