package cz.cvut.fit.poberboh.loc_tracker.network.api

import cz.cvut.fit.poberboh.loc_tracker.network.responses.LocationResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BasicApi {
    @GET("api/v1/incidents")
    suspend fun getIncidents(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): List<LocationResponse>
}