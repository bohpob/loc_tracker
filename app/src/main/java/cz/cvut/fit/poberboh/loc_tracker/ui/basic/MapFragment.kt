package cz.cvut.fit.poberboh.loc_tracker.ui.basic

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.preference.PreferenceManager
import cz.cvut.fit.poberboh.loc_tracker.R
import cz.cvut.fit.poberboh.loc_tracker.databinding.FragmentMapBinding
import cz.cvut.fit.poberboh.loc_tracker.network.Resource
import cz.cvut.fit.poberboh.loc_tracker.network.api.BasicApi
import cz.cvut.fit.poberboh.loc_tracker.network.responses.LocationResponse
import cz.cvut.fit.poberboh.loc_tracker.repository.BasicRepository
import cz.cvut.fit.poberboh.loc_tracker.ui.base.BaseFragment
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class MapFragment : BaseFragment<BasicViewModel, FragmentMapBinding, BasicRepository>() {

    private lateinit var myLocationProvider: GpsMyLocationProvider
    private lateinit var myLocationOverlay: MyLocationNewOverlay
    private lateinit var mapView: MapView
    private lateinit var incidentIcon : Drawable
    private val locationUpdateTime = 500L
    private val zoomLevel = 18.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        loadMapView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.incident_24)
        if (drawable != null) {
            incidentIcon = BitmapDrawable(requireContext().resources, drawable.toBitmap())
        }

        viewModel.incidents.observe(viewLifecycleOwner) { incidents ->
            when (incidents) {
                is Resource.Success -> displayIncidentsOnMap(incidents.data)
                is Resource.Error -> displayIncidentsOnMap(emptyList())
            }
        }
    }

    private fun loadMapView() {
        Configuration.getInstance()
            .load(requireContext(), PreferenceManager.getDefaultSharedPreferences(requireContext()))

        mapView = binding.mapView

        setupMapView()
        setupCompass()
        setupLocationOverlay()
        mapView.visibility = MapView.VISIBLE

        viewModel.startIncidentsUpdate()
    }

    private fun displayIncidentsOnMap(incidents: List<LocationResponse>) {
        mapView.overlays.removeIf { it is Marker }

        for (incident in incidents) {
            val customMarker = Marker(mapView)
            customMarker.icon = incidentIcon
            customMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            customMarker.position = GeoPoint(incident.latitude, incident.longitude)
            mapView.overlays.add(customMarker)
        }

        mapView.invalidate()
    }


    private fun setupMapView() {
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.controller.setZoom(zoomLevel)
        mapView.zoomController.setVisibility(CustomZoomButtonsController.Visibility.ALWAYS)
        mapView.setMultiTouchControls(true)
    }

    private fun setupCompass() {
        val compassOverlay = CompassOverlay(
            requireContext(),
            InternalCompassOrientationProvider(requireContext()),
            mapView
        )
        compassOverlay.enableCompass()
        mapView.overlays.add(compassOverlay)
    }

    private fun setupLocationOverlay() {
        myLocationProvider = GpsMyLocationProvider(requireContext())
        myLocationProvider.locationUpdateMinTime = locationUpdateTime

        myLocationOverlay = object : MyLocationNewOverlay(myLocationProvider, mapView) {
            override fun onLocationChanged(location: Location?, source: IMyLocationProvider?) {
                super.onLocationChanged(location, source)

                location?.let { updatedLocation ->
                    val geoPoint = GeoPoint(updatedLocation.latitude, updatedLocation.longitude)
                    viewModel.updateCurrentLocation(GeoPoint(geoPoint))
                    mapView.controller.setCenter(geoPoint)
                }
            }
        }
        myLocationOverlay.enableMyLocation(myLocationProvider)
        mapView.overlays.add(myLocationOverlay)
    }

    override fun getViewModel() = BasicViewModel::class.java

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMapBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() =
        BasicRepository(remoteDataSource.buildApi(BasicApi::class.java))
}