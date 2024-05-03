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
import cz.cvut.fit.poberboh.loc_tracker.network.api.MapApi
import cz.cvut.fit.poberboh.loc_tracker.network.responses.LocationResponse
import cz.cvut.fit.poberboh.loc_tracker.repository.MapRepository
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

/**
 * Fragment class for the map screen of the application.
 */
class MapFragment : BaseFragment<MapViewModel, FragmentMapBinding, MapRepository>() {

    private lateinit var myLocationProvider: GpsMyLocationProvider
    private lateinit var myLocationOverlay: MyLocationNewOverlay
    private lateinit var mapView: MapView
    private lateinit var incidentIcon : Drawable

    // Location update interval in milliseconds
    private val locationUpdateTime = 500L
    // Initial zoom level for the map
    private val zoomLevel = 18.0

    /**
     * Called to create the view hierarchy associated with the fragment.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        loadMapView()
        return binding.root
    }

    /**
     * Called when the view is created.
     * @param view The view returned by onCreateView.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the incident icon for the map
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.incident_24)
        if (drawable != null) {
            incidentIcon = BitmapDrawable(requireContext().resources, drawable.toBitmap())
        }

        // Observe the incidents LiveData and display them on the map
        viewModel.incidents.observe(viewLifecycleOwner) { incidents ->
            when (incidents) {
                is Resource.Success -> displayIncidentsOnMap(incidents.data)
                is Resource.Error -> displayIncidentsOnMap(emptyList())
            }
        }
    }

    /**
     * Load the map view and set up the map.
     */
    private fun loadMapView() {
        // Load the configuration settings for the map
        Configuration.getInstance()
            .load(requireContext(), PreferenceManager.getDefaultSharedPreferences(requireContext()))

        mapView = binding.mapView

        setupMapView()
        setupCompass()
        setupLocationOverlay()
        mapView.visibility = MapView.VISIBLE

        // Start updating the incidents
        viewModel.startIncidentsUpdate()
    }

    /**
     * Display the incidents on the map.
     * @param incidents The list of incidents to display.
     */
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

    /**
     * Set up the map view.
     */
    private fun setupMapView() {
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.controller.setZoom(zoomLevel)
        mapView.zoomController.setVisibility(CustomZoomButtonsController.Visibility.ALWAYS)
        mapView.setMultiTouchControls(true)
    }

    /**
     * Set up the compass overlay for the map.
     */
    private fun setupCompass() {
        val compassOverlay = CompassOverlay(
            requireContext(),
            InternalCompassOrientationProvider(requireContext()),
            mapView
        )
        compassOverlay.enableCompass()
        mapView.overlays.add(compassOverlay)
    }

    /**
     * Set up the location overlay for the map.
     */
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

    /**
     * Get the ViewModel associated with this fragment.
     * @return The ViewModel class.
     */
    override fun getViewModel() = MapViewModel::class.java

    /**
     * Inflate the binding layout for this fragment.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @return The binding object for the fragment's layout.
     */
    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMapBinding.inflate(inflater, container, false)

    /**
     * Get the repository associated with this fragment.
     * @return The repository object.
     */
    override fun getFragmentRepository() =
        MapRepository(remoteDataSource.buildApi(MapApi::class.java))
}