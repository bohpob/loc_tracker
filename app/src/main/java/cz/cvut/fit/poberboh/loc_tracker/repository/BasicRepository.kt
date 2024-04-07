package cz.cvut.fit.poberboh.loc_tracker.repository

import cz.cvut.fit.poberboh.loc_tracker.network.api.BasicApi
import cz.cvut.fit.poberboh.loc_tracker.network.requests.LocationRequest

class BasicRepository(private val api: BasicApi) : BaseRepository() {

    suspend fun getIncidents(latitude: String, longitude: String) = safeApiCall {
        api.getIncidents(request = LocationRequest(latitude = latitude, longitude = longitude))
    }
}