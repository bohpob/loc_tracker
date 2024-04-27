package cz.cvut.fit.poberboh.loc_tracker.ui.basic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.poberboh.loc_tracker.network.Resource
import cz.cvut.fit.poberboh.loc_tracker.network.responses.LocationResponse
import cz.cvut.fit.poberboh.loc_tracker.repository.BasicRepository
import cz.cvut.fit.poberboh.loc_tracker.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import java.util.Timer
import java.util.TimerTask

class BasicViewModel(private val repository: BasicRepository) : BaseViewModel(repository) {
    private val _location: MutableLiveData<GeoPoint> = MutableLiveData()
    private val _incidents: MutableLiveData<Resource<List<LocationResponse>>> = MutableLiveData()

    private var timer: Timer? = null
    private val incidentsUpdateInterval = 2000L

    val incidents: LiveData<Resource<List<LocationResponse>>> get() = _incidents

    private fun getIncidents() = viewModelScope.launch {
        if (_location.value != null) {
            _incidents.value = repository.getIncidents(
                _location.value!!.latitude,
                _location.value!!.longitude
            )
        }
    }

    fun startIncidentsUpdate() {
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                getIncidents()
            }
        }, incidentsUpdateInterval, incidentsUpdateInterval)
    }

    fun updateCurrentLocation(point: GeoPoint) = viewModelScope.launch {
        _location.value = point
    }
}