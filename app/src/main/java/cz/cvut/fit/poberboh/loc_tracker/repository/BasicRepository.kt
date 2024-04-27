package cz.cvut.fit.poberboh.loc_tracker.repository

import cz.cvut.fit.poberboh.loc_tracker.network.api.BasicApi

class BasicRepository(private val api: BasicApi) : BaseRepository() {

    suspend fun getIncidents(latitude: Double, longitude: Double) = safeApiCall {
        api.getIncidents(latitude = latitude, longitude = longitude)
    }
}