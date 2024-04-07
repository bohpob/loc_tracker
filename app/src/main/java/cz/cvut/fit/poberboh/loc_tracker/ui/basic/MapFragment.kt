package cz.cvut.fit.poberboh.loc_tracker.ui.basic

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import cz.cvut.fit.poberboh.loc_tracker.databinding.FragmentMapBinding
import cz.cvut.fit.poberboh.loc_tracker.network.api.BasicApi
import cz.cvut.fit.poberboh.loc_tracker.repository.BasicRepository
import cz.cvut.fit.poberboh.loc_tracker.ui.base.BaseFragment
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class MapFragment : BaseFragment<BasicViewModel, FragmentMapBinding, BasicRepository>() {

    private lateinit var myLocationProvider: GpsMyLocationProvider
    private lateinit var myLocationOverlay: MyLocationNewOverlay
    private val requestPermissionsCode = 1
    private lateinit var mapView: MapView
    private val locationUpdateTime = 500L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        loadMapView()
        requestPermissionsIfNecessary()
        return binding.root
    }

    private fun loadMapView() {
        Configuration.getInstance()
            .load(requireContext(), PreferenceManager.getDefaultSharedPreferences(requireContext()))

        mapView = binding.mapView

        setupMapView()
        setupCompass()
        setupLocationOverlay()
        mapView.visibility = MapView.VISIBLE
    }

    private fun setupMapView() {
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.controller.setZoom(18.0)
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

    private fun requestPermissionsIfNecessary() {
        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.INTERNET
        )

        val permissionsToRequest = ArrayList<String>()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(requireContext(), permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsToRequest.add(permission)
            }
        }
        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                permissionsToRequest.toTypedArray(),
                requestPermissionsCode
            )
        }
    }

    override fun getViewModel() = BasicViewModel::class.java

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMapBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() =
        BasicRepository(remoteDataSource.buildApi(BasicApi::class.java))
}