package cz.cvut.fit.poberboh.loc_tracker.network.api

import cz.cvut.fit.poberboh.loc_tracker.network.requests.LocationRequest
import cz.cvut.fit.poberboh.loc_tracker.network.responses.LocationResponse
import retrofit2.http.Body
import retrofit2.http.GET

interface BasicApi {
    @GET("api/v1/incidents/locations")
    suspend fun getIncidents(@Body request: LocationRequest): List<LocationResponse>
}