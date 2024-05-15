package cz.cvut.fit.poberboh.loc_tracker.network

import cz.cvut.fit.poberboh.loc_tracker.network.api.MapApi
import cz.cvut.fit.poberboh.loc_tracker.network.responses.LocationResponse
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSourceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var mapApi: MapApi

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        // Create a Retrofit instance with MockWebServer as the base URL
        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Create an instance of MapApi using the Retrofit instance
        mapApi = retrofit.create(MapApi::class.java)

        remoteDataSource = RemoteDataSource()
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testGetIncidentsSuccess() {
        // Enqueue a mock response for successful API call
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("[]") // JSON response body representing an empty list of incidents
        mockWebServer.enqueue(mockResponse)

        // Call the API method
        val result = runBlocking {
            mapApi.getIncidents(0.0, 0.0)
        }

        // Verify that the request was sent to the correct path
        val request = mockWebServer.takeRequest()
        assertEquals("/api/v1/incidents?latitude=0.0&longitude=0.0", request.path)

        // Verify that the result is empty list of incidents
        assertEquals(emptyList<LocationResponse>(), result)
    }

    @Test
    fun testGetIncidentsFailure() {
        // Enqueue a mock response for failed API call
        val mockResponse = MockResponse().setResponseCode(500) // Internal server error
        mockWebServer.enqueue(mockResponse)

        // Call the API method
        val result = runBlocking {
            try {
                mapApi.getIncidents(0.0, 0.0)
            } catch (e: HttpException) {
                // Handle the HttpException here if needed
                null // Return null for simplicity in this example
            }
        }

        // Verify that the request was sent to the correct path
        val request = mockWebServer.takeRequest()
        assertEquals("/api/v1/incidents?latitude=0.0&longitude=0.0", request.path)

        // Verify that the result is null
        assertNull(result)
    }

    @Test
    fun testGetIncidentsFailureWithHTTP404Response() {
        // Enqueue a mock response with HTTP 404 (Not Found) status code
        val mockResponse = MockResponse().setResponseCode(404)
        mockWebServer.enqueue(mockResponse)

        // Call the API method
        val result = runBlocking {
            try {
                mapApi.getIncidents(0.0, 0.0)
            } catch (e: HttpException) {
                // Handle the HttpException here if needed
                null // Return null for simplicity in this example
            }
        }

        // Verify that the request was sent to the correct path
        val request = mockWebServer.takeRequest()
        assertEquals("/api/v1/incidents?latitude=0.0&longitude=0.0", request.path)

        // Verify that the result is null
        assertNull(result)
    }


}
