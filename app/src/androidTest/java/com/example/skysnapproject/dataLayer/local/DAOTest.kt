package com.example.skysnapproject.dataLayer.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.skysnapproject.dataLayer.models.Place
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@SmallTest
class DAOTest {

    private lateinit var dao : DAO
    private lateinit var database: PlaceDatabase

    @Before
    fun  setup(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PlaceDatabase::class.java
        ).build()
        dao = database.placeDao()
    }

    @After
    fun dearDown() = database.close()

    @Test
    fun getFavPlaces_insertPlace_getThePlace() = runTest{

        // Given

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
        dao.insert(place1)
        dao.insert(place2)

        // when
        val result = dao.getFavPlaces().first()



        //then
        assertEquals(2, result.size)


        assertThat(result[0].id, `is`(place1.id))
        assertThat(result[0].name, `is`(place1.name))
        assertThat(result[0].lat, `is`(place1.lat))
        assertThat(result[0].lng, `is`(place1.lng))

        assertThat(result[1].id, `is`(place2.id))
        assertThat(result[1].name, `is`(place2.name))
        assertThat(result[1].lat, `is`(place2.lat))
        assertThat(result[1].lng, `is`(place2.lng))


    }




    @Test

    fun delete_insertPlaceAndDeleteIt_checkDeletion() = runTest {

        // Given
        val place1 = Place(
            lat = 30.0121, lng = 29.05445,
            id = 111,
            name = "myLocation"
        )

       //  when
        dao.insert(place1)
        val ans = dao.delete(place1)

        // then
        assertThat(ans, `is`(1))

    }




}