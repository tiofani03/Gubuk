package com.tegar.gubuk.ui.maps

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.BounceInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute
import com.tegar.gubuk.R
import com.tegar.gubuk.databinding.ActivityMapsBinding
import com.tegar.gubuk.model.BookStore
import com.tegar.gubuk.utils.Helper.toast
import com.tegar.gubuk.utils.Helper.toolbar
import retrofit2.Call
import retrofit2.Response

class MapsActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_MAP = "extra map"
        private const val ICON_ID = "ICON_ID"
    }

    private lateinit var binding: ActivityMapsBinding
    private lateinit var mapboxMap: MapboxMap
    private lateinit var symbolManager: SymbolManager
    private lateinit var locationComponent: LocationComponent
    private lateinit var myLocation: LatLng
    private lateinit var permissionManager: PermissionsManager
    private lateinit var navigationMapRoute: NavigationMapRoute
    private var currentRoute: DirectionsRoute? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.access_token))
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync { mapboxMap ->
            this.mapboxMap = mapboxMap
            mapboxMap.setStyle(Style.MAPBOX_STREETS) { style ->
                symbolManager = SymbolManager(binding.mapView, mapboxMap, style)
                symbolManager.iconAllowOverlap = true

                style.addImage(
                    ICON_ID,
                    BitmapFactory.decodeResource(resources, R.drawable.mapbox_marker_icon_default)
                )

                navigationMapRoute = NavigationMapRoute(
                    null,
                    binding.mapView,
                    mapboxMap,
                    R.style.NavigationMapRoute
                )



                showMyLocation(style)


            }
        }
    }

    private fun initTitle(title: String) {
        supportActionBar?.title = "Lokasi $title"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun showBookStoreLocation() {
        val extraMap = intent.getParcelableExtra<BookStore>(EXTRA_MAP)
        if (extraMap != null) {
            initTitle(extraMap.name.toString())
            val latitude = extraMap.lat?.toDouble()
            val longitude = extraMap.long?.toDouble()
            val location = LatLng(latitude!!, longitude!!)


            symbolManager.create(
                SymbolOptions()
                    .withLatLng(LatLng(location.latitude, location.longitude))
                    .withIconImage(ICON_ID)
                    .withIconSize(1.5f)
                    .withIconOffset(arrayOf(0f, -1.5f))
                    .withTextField(extraMap.name)
                    .withTextHaloColor("rgba(255,255,255,100)")
                    .withTextHaloWidth(5.0f)
                    .withTextAnchor("top")
                    .withTextOffset(arrayOf(0f, 1.5f))
                    .withDraggable(false)
            )
            val destination = Point.fromLngLat(longitude, latitude)
            val origin = Point.fromLngLat(myLocation.longitude, myLocation.latitude)
            requestRoute(origin, destination)
            mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 8.0))

        }


    }


    @SuppressLint("MissingPermission")
    private fun showMyLocation(style: Style) {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            val locationComponentOptions = LocationComponentOptions.builder(this)
                .pulseEnabled(true)
                .pulseColor(Color.BLUE)
                .pulseAlpha(.4f)
                .pulseInterpolator(BounceInterpolator())
                .build()

            val locationComponentActivationOptions = LocationComponentActivationOptions
                .builder(this, style)
                .locationComponentOptions(locationComponentOptions)
                .build()

            locationComponent = mapboxMap.locationComponent
            locationComponent.activateLocationComponent(locationComponentActivationOptions)
            locationComponent.isLocationComponentEnabled = true
            locationComponent.cameraMode = CameraMode.TRACKING
            locationComponent.renderMode = RenderMode.COMPASS

            myLocation = LatLng(
                locationComponent.lastKnownLocation?.latitude as Double,
                locationComponent.lastKnownLocation?.longitude as Double
            )
            mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 12.0))
            showBookStoreLocation()
            showNavigation()
        } else {
            permissionManager = PermissionsManager(object : PermissionsListener {
                override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
                    toast("Anda harus mengizinkan location permission untuk menggunakan aplikasi ini")
                }

                override fun onPermissionResult(granted: Boolean) {
                    if (granted) {
                        mapboxMap.getStyle { style ->
                            showMyLocation(style)
                        }
                    } else {
                        finish()
                    }
                }
            })

            permissionManager.requestLocationPermissions(this)
        }

    }


    private fun requestRoute(origin: Point, destination: Point) {
        navigationMapRoute.updateRouteVisibilityTo(false)
        NavigationRoute.builder(this)
            .accessToken(getString(R.string.access_token))
            .origin(origin)
            .destination(destination)
            .build()
            .getRoute(object : retrofit2.Callback<DirectionsResponse> {
                override fun onResponse(
                    call: Call<DirectionsResponse>,
                    response: Response<DirectionsResponse>
                ) {
                    if (response.body() == null) {
                        toast("Tidak ada rute cek token")
                        return
                    } else if (response.body()?.routes()?.size == 0) {
                        toast("Tidak ada rute ditemukan")
                        return
                    }

                    currentRoute = response.body()?.routes()?.get(0)
                    navigationMapRoute.addRoute(currentRoute)
                }

                override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
                    toast("Error : $t")
                }

            })
    }

    private fun showNavigation() {
        binding.btnNavigation.visibility = View.VISIBLE
        binding.btnNavigation.setOnClickListener {
            val simulateRoute = true

            val options = NavigationLauncherOptions.builder()
                .directionsRoute(currentRoute)
                .shouldSimulateRoute(simulateRoute)
                .build()

            NavigationLauncher.startNavigation(this, options)
        }
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}