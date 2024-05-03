package cz.cvut.fit.poberboh.loc_tracker.network.responses

/**
 * Data class representing a location response with latitude and longitude coordinates.
 * @property latitude The latitude coordinate.
 * @property longitude The longitude coordinate.
 */
data class LocationResponse(
    val latitude: Double,
    val longitude: Double
)
