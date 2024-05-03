package cz.cvut.fit.poberboh.loc_tracker.network.api

import cz.cvut.fit.poberboh.loc_tracker.network.responses.LocationResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface defining API endpoints related to map and incident data.
 */
interface MapApi {
    /**
     * Fetches incidents near a specific latitude and longitude coordinate.
     * @param latitude The latitude coordinate.
     * @param longitude The longitude coordinate.
     * @return A list of [LocationResponse] objects representing incident locations.
     */
    @GET("api/v1/incidents")
    suspend fun getIncidents(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): List<LocationResponse>
}