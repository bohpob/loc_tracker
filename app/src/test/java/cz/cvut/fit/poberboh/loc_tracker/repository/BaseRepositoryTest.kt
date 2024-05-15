package cz.cvut.fit.poberboh.loc_tracker.repository

import cz.cvut.fit.poberboh.loc_tracker.network.Resource
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import java.net.ConnectException


class BaseRepositoryTest {

    private lateinit var baseRepository: BaseRepository

    @Before
    fun setUp() {
        baseRepository = object : BaseRepository() {}
    }


    @Test
    fun safeApiCall_shouldReturnResourceSuccess_whenApiCallSucceeds() = runTest {
        // Arrange
        val expectedData = "Success"
        val apiCall: suspend () -> String = { expectedData }

        // Act
        val result = baseRepository.safeApiCall(apiCall)

        // Assert
        assertTrue(result is Resource.Success)
        assertEquals(expectedData, (result as Resource.Success).data)
    }

    @Test
    fun safeApiCall_shouldReturnResourceError_forConnectException() = runTest {
        // Arrange
        val apiCall: suspend () -> String = { throw ConnectException() }

        // Act
        val result = baseRepository.safeApiCall(apiCall)

        // Assert
        assertTrue(result is Resource.Error)
        assertEquals(false, (result as Resource.Error).isNetworkError)
        assertEquals("Failed to connect", result.errorBody?.string())
    }

    @Test
    fun safeApiCall_shouldReturnResourceError_forHttpException() = runTest {
        // Arrange
        val errorCode = 404
        val apiCall: suspend () -> String = {
            throw HttpException(
                retrofit2.Response.error<String>(
                    errorCode,
                    "".toResponseBody("text/plain".toMediaTypeOrNull())
                )
            )
        }

        // Act
        val result = baseRepository.safeApiCall(apiCall)

        // Assert
        assertTrue(result is Resource.Error)
        assertEquals(false, (result as Resource.Error).isNetworkError)
        assertEquals(errorCode, result.errorCode)
    }

    @Test
    fun safeApiCall_shouldReturnResourceError_forOtherExceptions() = runTest {
        // Arrange
        val apiCall: suspend () -> String = { throw Exception() }

        // Act
        val result = baseRepository.safeApiCall(apiCall)

        // Assert
        assertTrue(result is Resource.Error)
        assertEquals(true, (result as Resource.Error).isNetworkError)
    }
}