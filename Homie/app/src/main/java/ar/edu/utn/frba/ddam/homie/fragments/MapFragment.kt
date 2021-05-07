package ar.edu.utn.frba.ddam.homie.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import ar.edu.utn.frba.ddam.homie.R
import ar.edu.utn.frba.ddam.homie.database.LocalDatabase
import ar.edu.utn.frba.ddam.homie.helpers.Utils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.text.NumberFormat


class MapFragment : Fragment() {
    private lateinit var v : View
    private lateinit var mvAllPosts : MapView
    private lateinit var localDB : LocalDatabase

    private lateinit var markers : MutableMap<Int, MarkerOptions>

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
                cvMapDetail.visibility = View.VISIBLE
                val post = localDB.postDao().getById(marker.tag as Int)!!
                val building = post.getBuilding(v.context)!!

                Utils.setImage(v.context, v, ivMapImage, null, if(building.images.count() > 0) building.images.first() else "", R.drawable.no_image, resources.getString(R.string.error_post_detail_image))

                tvMapBuildingType.text = Utils.getString(v.context, "buildings_types_${building.type}")
                tvMapPostType.text = Utils.getString(v.context, "posts_types_${post.type}")
                tvMapTotalSize.text = "${building.surfaceOpen.toString()} m²"
                tvMapRooms.text = "${building.rooms} ${resources.getString(R.string.room)}${if(building.rooms > 1) "s" else ""}"
                tvMapPrice.text = post.getPrice()

                cvMapDetail.setOnClickListener {
                    val action = MapFragmentDirections.mapToPostDetail(marker.tag as Int)
                    v.findNavController().navigate(action)
                }

                false
            }
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
}