package ar.edu.utn.frba.ddam.homie.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import ar.edu.utn.frba.ddam.homie.R
import ar.edu.utn.frba.ddam.homie.database.LocalDatabase
import ar.edu.utn.frba.ddam.homie.helpers.Utils
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import java.text.NumberFormat


class MapFragment : Fragment() {
    val PERMISSION_ID = 42
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var v : View
    private lateinit var mvAllPosts : MapView
    private lateinit var localDB : LocalDatabase

    private lateinit var markers : MutableMap<Int, MarkerOptions>
    private var marker_you : Marker? = null
    private var last_location : LatLng? = null

    private lateinit var cvMapDetail : CardView
    private lateinit var ivMapImage : ImageView
    private lateinit var tvMapBuildingType : TextView
    private lateinit var tvMapPostType : TextView
    private lateinit var tvMapTotalSize : TextView
    private lateinit var tvMapRooms : TextView
    private lateinit var tvMapPrice : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_map, container, false)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(v.context)

        localDB = LocalDatabase.getLocalDatabase(v.context)!!
        mvAllPosts = v.findViewById(R.id.mvAllPosts)

        cvMapDetail = v.findViewById(R.id.cvMapDetail)
        ivMapImage = v.findViewById(R.id.ivMapImage)
        tvMapBuildingType = v.findViewById(R.id.tvMapBuildingType)
        tvMapPostType = v.findViewById(R.id.tvMapPostType)
        tvMapTotalSize = v.findViewById(R.id.tvMapTotalSize)
        tvMapRooms = v.findViewById(R.id.tvMapRooms)
        tvMapPrice = v.findViewById(R.id.tvMapPrice)

        markers = mutableMapOf<Int, MarkerOptions>()
        val posts = localDB.postDao().getAll();
        for(post in posts) {
            val building = post.getBuilding(v.context)!!
            val location = building.getLocation(v.context)!!;

            if(location.latitude != 0.0 && location.longitude != 0.0)
                markers[post.id] = MarkerOptions()
                    .position(LatLng(location.latitude, location.longitude))
                    .title(Utils.getString(v.context, "posts_types_${post.type}"))
                    .snippet("${post.currency} ${NumberFormat.getIntegerInstance().format(post.price).replace(",", ".")}")
        }
        return v
    }

    override fun onStart() {
        super.onStart()

        mvAllPosts.onCreate(null)
        mvAllPosts.getMapAsync { map ->
            MapsInitializer.initialize(v.context)
            for(postId in markers.keys) {
                map.addMarker(markers[postId])
                    .tag = postId
            }

            if(markers.keys.count() > 0) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(markers[markers.keys.first()]!!.position, 12f))
            } else {
                val defaultLoc = LatLng(-34.59127073579342, -58.459087290471494)
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLoc, 12f))
            }

            map.mapType= GoogleMap.MAP_TYPE_NORMAL

            map.setOnMarkerClickListener { marker ->
                if(marker.tag != null && marker.tag != "") {
                    cvMapDetail.visibility = View.VISIBLE
                    val post = localDB.postDao().getById(marker.tag as Int)!!
                    val building = post.getBuilding(v.context)!!

                    Utils.setImage(v.context, v, ivMapImage, null, if (building.images.count() > 0) building.images.first() else "", R.drawable.no_image, resources.getString(R.string.error_post_detail_image))

                    tvMapBuildingType.text = Utils.getString(v.context, "buildings_types_${building.type}")
                    tvMapPostType.text = Utils.getString(v.context, "posts_types_${post.type}")
                    tvMapTotalSize.text = "${building.surfaceOpen.toString()} m²"
                    tvMapRooms.text = "${building.rooms} ${resources.getString(R.string.room)}${if (building.rooms > 1) "s" else ""}"
                    tvMapPrice.text = post.getPrice()

                    cvMapDetail.setOnClickListener {
                        val action = MapFragmentDirections.mapToPostDetail(marker.tag as Int)
                        v.findNavController().navigate(action)
                    }
                }
                false
            }
            getLastLocation();
        }
    }

    override fun onResume() {
        super.onResume()
        mvAllPosts.onResume();
    }

    override fun onPause() {
        super.onPause()
        mvAllPosts.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mvAllPosts.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mvAllPosts.onLowMemory()
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location : Location? ->
                        if (location == null) {
                            requestNewLocationData()
                        } else {
                            mvAllPosts.getMapAsync { map ->
                                var setLocation : Boolean = true
                                if(last_location != null) {
                                    val punto1 = Location("");
                                    punto1.latitude = location.latitude
                                    punto1.longitude = location.longitude

                                    val punto2 = Location("");
                                    punto2.latitude = last_location!!.latitude
                                    punto2.longitude = last_location!!.longitude

                                    if(punto2.distanceTo(punto1) < 20) setLocation = false;
                                }

                                if(setLocation) {
                                    if (marker_you != null) marker_you!!.remove();
                                    val you_location = LatLng(location.latitude, location.longitude)
                                    last_location = you_location
                                    val marker = MarkerOptions()
                                            .position(you_location)
                                            .title(Utils.getString(v.context, "you"))
                                    marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                                    marker_you = map.addMarker(marker);
                                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(you_location, 12f))
                                    requestNewLocationData()
                                }
                            }
                        }
                    }
            } else {
                Snackbar.make(v, resources.getString(R.string.turn_on_location), Snackbar.LENGTH_SHORT).show();
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(v.context)
        fusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper()).addOnCompleteListener {
            getLastLocation()
        }
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = v.context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                        v.context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                        v.context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_ID
        )
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }
}