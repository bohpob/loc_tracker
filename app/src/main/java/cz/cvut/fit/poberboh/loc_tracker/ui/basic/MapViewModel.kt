package cz.cvut.fit.poberboh.loc_tracker.ui.basic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.poberboh.loc_tracker.network.Resource
import cz.cvut.fit.poberboh.loc_tracker.network.responses.LocationResponse
import cz.cvut.fit.poberboh.loc_tracker.repository.MapRepository
import cz.cvut.fit.poberboh.loc_tracker.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import java.util.Timer
import java.util.TimerTask

/**
 * ViewModel class for the MapFragment.
 * @param repository The repository associated with this ViewModel.
 */
class MapViewModel(private val repository: MapRepository) : BaseViewModel(repository) {
    private val _location: MutableLiveData<GeoPoint> = MutableLiveData()
    private val _incidents: MutableLiveData<Resource<List<LocationResponse>>> = MutableLiveData()

    // Timer for fetching incidents
    private var timer: Timer? = null
    // Update interval for fetching incidents in milliseconds
    private val incidentsUpdateInterval = 1000L

    val incidents: LiveData<Resource<List<LocationResponse>>> get() = _incidents

    /**
     * Fetches the incidents data from the repository based on the current location.
     */
    private fun getIncidents() = viewModelScope.launch {
        if (_location.value != null) {
            _incidents.value = repository.getIncidents(
                _location.value!!.latitude,
                _location.value!!.longitude
            )
        }
    }

    /**
     * Starts updating the incidents periodically.
     */
    fun startIncidentsUpdate() {
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                getIncidents()
            }
        }, incidentsUpdateInterval, incidentsUpdateInterval)
    }

    /**
     * Updates the current location.
     * @param point The new location.
     */
    fun updateCurrentLocation(point: GeoPoint) = viewModelScope.launch {
        _location.value = point
    }
}