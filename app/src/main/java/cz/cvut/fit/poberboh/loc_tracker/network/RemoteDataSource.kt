package cz.cvut.fit.poberboh.loc_tracker.network

import cz.cvut.fit.poberboh.loc_tracker.BuildConfig
import cz.cvut.fit.poberboh.loc_tracker.BuildConfig.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Class responsible for building Retrofit APIs.
 */
class RemoteDataSource {

    /**
     * Builds and returns an instance of the specified Retrofit API.
     * @param api The API interface class.
     * @return An instance of the specified Retrofit API.
     */
    fun <Api> buildApi(api: Class<Api>): Api {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(api)
    }

    /**
     * Configures and returns an OkHttpClient instance.
     * @return An OkHttpClient instance.
     */
    private fun getClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .apply {
                if (BuildConfig.DEBUG) { // Add logging interceptor for debug builds
                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                    addInterceptor(logging)
                }
            }
            .build()
    }
}
