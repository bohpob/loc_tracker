package cz.cvut.fit.poberboh.loc_tracker.repository

import cz.cvut.fit.poberboh.loc_tracker.network.api.MapApi

/**
 * Repository class for handling map-related API calls.
 * @param api The instance of [MapApi] used for making API calls.
 */
class MapRepository(private val api: MapApi) : BaseRepository() {

    /**
     * Retrieves incidents near a specified location.
     * @param latitude The latitude of the location.
     * @param longitude The longitude of the location.
     * @return A [Resource] object representing the result of the API call.
     */
    suspend fun getIncidents(latitude: Double, longitude: Double) = safeApiCall {
        api.getIncidents(latitude = latitude, longitude = longitude)
    }
}