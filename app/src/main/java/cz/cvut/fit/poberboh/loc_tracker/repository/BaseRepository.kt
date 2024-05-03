package cz.cvut.fit.poberboh.loc_tracker.repository

import cz.cvut.fit.poberboh.loc_tracker.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import java.net.ConnectException

/**
 * Abstract base repository class for handling API calls and network exceptions.
 */
abstract class BaseRepository {
    /**
     * Executes a function representing an API call in a safe manner.
     * @param apiCall The suspend function representing the API call.
     * @return A [Resource] object representing the result of the API call.
     */
    suspend fun <T> safeApiCall(apiCall: suspend () -> T): Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                Resource.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is ConnectException -> {
                        // Return a Resource.Error for network connection failure
                        Resource.Error(
                            false,
                            null,
                            "Failed to connect".toResponseBody("text/plain".toMediaTypeOrNull())
                        )
                    }

                    is HttpException -> {
                        // Return a Resource.Error for HTTP error
                        Resource.Error(false, throwable.code(), throwable.response()?.errorBody())
                    }

                    else -> {
                        // Return a Resource.Error for other exceptions
                        Resource.Error(true, null, null)
                    }
                }
            }
        }
    }
}