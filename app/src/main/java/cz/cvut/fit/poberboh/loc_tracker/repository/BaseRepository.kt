package cz.cvut.fit.poberboh.loc_tracker.repository

import cz.cvut.fit.poberboh.loc_tracker.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import java.net.ConnectException

abstract class BaseRepository {
    suspend fun <T> safeApiCall(apiCall: suspend () -> T): Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                Resource.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is ConnectException -> {
                        Resource.Error(
                            false,
                            null,
                            "Failed to connect".toResponseBody("text/plain".toMediaTypeOrNull())
                        )
                    }

                    is HttpException -> {
                        Resource.Error(false, throwable.code(), throwable.response()?.errorBody())
                    }

                    else -> {
                        Resource.Error(true, null, null)
                    }
                }
            }
        }
    }
}