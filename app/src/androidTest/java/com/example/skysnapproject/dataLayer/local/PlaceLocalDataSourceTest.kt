package com.example.skysnapproject.dataLayer.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.skysnapproject.dataLayer.models.Place
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@MediumTest
class PlaceLocalDataSourceTest{

    private lateinit var database: PlaceDatabase
    private lateinit var dao: DAO
    private lateinit var localDataSource: PlaceLocalDataSource


    @Before
    fun setup(){

        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext()
        , PlaceDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        dao = database.placeDao()
        localDataSource = PlaceLocalDataSource(dao)
    }

    @After
    fun tearDown() = database.close()

    @Test
    fun getAllFavPlaces() = runTest{

        //Given
        val place1 = Place(
            lat = 30.0121, lng = 29.05445,
            id = 111,
            name = "myLocation"
        )
        val place2 = Place(
            lat = 40.0121, lng = 39.05445,
            id = 222,
            name = "mySecLocation"
        )
        localDataSource.insert(place1)
        localDataSource.insert(place2)

        //When
        val ans = localDataSource.getFavPlaces().first()

        // then
        assertThat(ans.size, `is`(2))
        assertThat(ans[0] , `is` (place1))
        assertThat(ans[1] , `is` (place2))

    }

    @Test
    fun checkDeleteFun() = runTest{

        //Given
        val place1 = Place(
            lat = 30.0121, lng = 29.05445,
            id = 111,
            name = "myLocation"
        )

        //When
        localDataSource.insert(place1)
        val ans = localDataSource.delete(place1)


        // then
        assertThat(ans, `is`(1))

    }


}