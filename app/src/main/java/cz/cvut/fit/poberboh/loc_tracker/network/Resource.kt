package cz.cvut.fit.poberboh.loc_tracker.network

import cz.cvut.fit.poberboh.loc_tracker.network.Resource.Error
import cz.cvut.fit.poberboh.loc_tracker.network.Resource.Success
import okhttp3.ResponseBody

/**
 * Sealed class representing a network resource.
 * It can either be [Success] containing the successful result,
 * or [Error] containing error information.
 */
sealed class Resource<out T> {
    /**
     * Represents a successful network call result.
     * @property data The successful data returned by the network call.
     */
    data class Success<out T>(val data: T) : Resource<T>()

    /**
     * Represents an error in a network call.
     * @property isNetworkError Boolean indicating if the error is due to network issues.
     * @property errorCode The error code returned by the server, if any.
     * @property errorBody The error response body returned by the server, if any.
     */
    data class Error(
        val isNetworkError: Boolean,
        val errorCode: Int?,
        val errorBody: ResponseBody?
    ) : Resource<Nothing>()
}