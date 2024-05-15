package cz.cvut.fit.poberboh.loc_tracker.repository

import cz.cvut.fit.poberboh.loc_tracker.network.Resource
import cz.cvut.fit.poberboh.loc_tracker.network.api.MapApi
import cz.cvut.fit.poberboh.loc_tracker.network.responses.LocationResponse
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MapRepositoryTest {

    private lateinit var mapApi: MapApi
    private lateinit var mapRepository: MapRepository

    @Before
    fun setup() {
        // Initialize the MapApi mock
        mapApi = mockk()
        // Create an instance of MapRepository with the mock MapApi
        mapRepository = MapRepository(mapApi)
    }

    @Test
    fun testGetIncidentsSuccess() {
        // Mock successful API response
        val locationResponseList = listOf<LocationResponse>()

        // Stub the API call
        every { runBlocking { mapApi.getIncidents(any(), any()) } } returns locationResponseList

        // Call the repository method
        val result: Resource<List<LocationResponse>>
        runBlocking { result = mapRepository.getIncidents(0.0, 0.0) }

        // Verify that the API method was called with correct parameters
        verify { runBlocking { mapApi.getIncidents(0.0, 0.0) } }

        // Verify that the result is of type Resource.Success and contains the expected data
        assertTrue(result is Resource.Success)
        assertEquals(locationResponseList, (result as Resource.Success).data)
    }

    @Test
    fun testGetIncidentsWithNon_EmptyList() {
        // Mock API response with a non-empty list of location responses
        val locationResponseList = listOf(LocationResponse(0.0, 0.0))
        every { runBlocking { mapApi.getIncidents(any(), any()) } } returns locationResponseList

        // Call the repository method
        val result: Resource<List<LocationResponse>>
        runBlocking { result = mapRepository.getIncidents(0.0, 0.0) }

        // Verify that the API method was called with correct parameters
        verify { runBlocking { mapApi.getIncidents(0.0, 0.0) } }

        // Verify that the result is of type Resource.Success and contains the expected data
        assertTrue(result is Resource.Success)
        assertEquals(locationResponseList, (result as Resource.Success).data)
    }

    @Test
    fun testGetIncidentsWithLatitudeAndLongitude() {
        // Mock API response for specific latitude and longitude
        val latitude = 30.0
        val longitude = 40.0
        val locationResponseList = listOf<LocationResponse>()
        every { runBlocking { mapApi.getIncidents(latitude, longitude) } } returns locationResponseList

        // Call the repository method
        val result: Resource<List<LocationResponse>>
        runBlocking { result = mapRepository.getIncidents(latitude, longitude) }

        // Verify that the API method was called with correct parameters
        verify { runBlocking { mapApi.getIncidents(latitude, longitude) } }

        // Verify that the result is of type Resource.Success and contains the expected data
        assertTrue(result is Resource.Success)
        assertEquals(locationResponseList, (result as Resource.Success).data)
    }
}

