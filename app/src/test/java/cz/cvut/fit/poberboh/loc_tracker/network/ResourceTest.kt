package cz.cvut.fit.poberboh.loc_tracker.network

import okhttp3.ResponseBody
import okio.BufferedSource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ResourceTest {

    @Test
    fun testSuccess() {
        // Create a sample data object
        val data = "Sample data"

        // Create a Success resource with the sample data
        val resource = Resource.Success(data)

        // Verify that the data in the resource is the same as the sample data
        assertEquals(data, resource.data)
    }

    @Test
    fun testError() {
        // Create error information
        val isNetworkError = true
        val errorCode = 404
        val errorBody: ResponseBody = FakeResponseBody("Error body")

        // Create an Error resource with the error information
        val resource = Resource.Error(isNetworkError, errorCode, errorBody)

        // Verify that the properties of the Error resource match the provided error information
        assertTrue(resource.isNetworkError)
        assertEquals(errorCode, resource.errorCode)
        assertEquals(errorBody, resource.errorBody)
    }

    // Simple implementation of ResponseBody for testing purposes
    private class FakeResponseBody(private val content: String) : ResponseBody() {
        override fun contentType() = null
        override fun contentLength() = content.length.toLong()
        override fun source(): BufferedSource {
            throw UnsupportedOperationException("Not implemented for testing purposes")
        }
        override fun toString() = content
    }
}
