package ar.edu.utn.frba.ddam.homie.fragments

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import ar.edu.utn.frba.ddam.homie.R
import ar.edu.utn.frba.ddam.homie.database.LocalDatabase
import ar.edu.utn.frba.ddam.homie.entities.Building
import ar.edu.utn.frba.ddam.homie.entities.Location
import ar.edu.utn.frba.ddam.homie.entities.Post
import ar.edu.utn.frba.ddam.homie.entities.User
import ar.edu.utn.frba.ddam.homie.ui.CustomMapView
import ar.edu.utn.frba.ddam.homie.helpers.Utils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text
import java.text.NumberFormat


class PostDetailInfoFragment(postId : Int) : Fragment() {
    private var postId : Int = postId

    private lateinit var v : View

    private lateinit var ivPostDetailImage : ImageView
    private lateinit var tvPostDetailType : TextView
    private lateinit var pbPostDetailLoading : ProgressBar
    private lateinit var tvPostDetailStatus : TextView

    private lateinit var tvPostDetailPrice : TextView
    private lateinit var tvPostDetailExpenses : TextView

    private lateinit var tvPostDetailTotalSize : TextView
    private lateinit var tvPostDetailCoveredSize : TextView
    private lateinit var tvPostDetailRooms : TextView
    private lateinit var tvPostDetailBathrooms : TextView
    private lateinit var tvPostDetailBedRooms : TextView
    private lateinit var tvPostDetailAge : TextView

    private lateinit var tvPostDetailAddress : TextView
    private lateinit var tvPostDetailDistrict : TextView
    private lateinit var tvPostDetailCity : TextView

    private lateinit var mvPostDetailMap : CustomMapView
    private lateinit var locationPosition : LatLng

    private lateinit var cvPostDetailWhatsApp : CardView

    private lateinit var mAuth : FirebaseAuth
    private lateinit var localDB : LocalDatabase

    private lateinit var user : User
    private lateinit var post : Post
    private lateinit var building : Building
    private lateinit var location : Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_post_detail_info, container, false)

        mAuth = FirebaseAuth.getInstance()
        localDB = LocalDatabase.getLocalDatabase(v.context)!!

        ivPostDetailImage = v.findViewById(R.id.ivPostDetailImage)
        tvPostDetailType = v.findViewById(R.id.tvPostDetailType)
        pbPostDetailLoading = v.findViewById(R.id.pbPostDetailLoading)
        tvPostDetailStatus = v.findViewById(R.id.tvPostDetailStatus)

        tvPostDetailPrice = v.findViewById(R.id.tvPostDetailPrice)
        tvPostDetailExpenses = v.findViewById(R.id.tvPostDetailExpenses)

        tvPostDetailTotalSize = v.findViewById(R.id.tvPostDetailTotalSize)
        tvPostDetailCoveredSize = v.findViewById(R.id.tvPostDetailCoveredSize)
        tvPostDetailRooms = v.findViewById(R.id.tvPostDetailRooms)
        tvPostDetailBathrooms = v.findViewById(R.id.tvPostDetailBathrooms)
        tvPostDetailBedRooms = v.findViewById(R.id.tvPostDetailBedRooms)
        tvPostDetailAge = v.findViewById(R.id.tvPostDetailAge)

        tvPostDetailAddress = v.findViewById(R.id.tvPostDetailAddress)
        tvPostDetailDistrict = v.findViewById(R.id.tvPostDetailDistrict)
        tvPostDetailCity = v.findViewById(R.id.tvPostDetailCity)

        mvPostDetailMap = v.findViewById(R.id.mvPostDetailMap)

        cvPostDetailWhatsApp = v.findViewById(R.id.cvPostDetailWhatsApp)

        user = localDB.userDao().getByDbId(mAuth.currentUser?.uid!!)!!
        post = localDB.postDao().getById(postId)!!
        building = post.getBuilding(v.context)!!
        location = building.getLocation(v.context)!!

        locationPosition = LatLng(location.latitude, location.longitude)

        cvPostDetailWhatsApp.setOnClickListener {
            val msg = "${resources.getString(R.string.whatsapp_contact_message).replace("{display_name}", user.getDisplayName()).replace("{address}", location.getFullAddress())}"
            Utils.sendWhatsApp(v, post.contactPhone, msg)
        }

        return v;
    }

    override fun onStart() {
        super.onStart()

        if(building.images.count() > 0) {
            Utils.setImage(v.context, v, ivPostDetailImage, pbPostDetailLoading, building.images.first(), R.drawable.no_image, resources.getString(R.string.error_post_detail_image))
        } else {
            pbPostDetailLoading.visibility = View.GONE
            ivPostDetailImage.setImageResource(R.drawable.no_image)
        }

        tvPostDetailType.text = "${Utils.getString(v.context, "posts_types_${post.type}")} · ${Utils.getString(v.context, "buildings_types_${building.type}")}"

        if (post.status == "reserved") {
            tvPostDetailStatus.visibility = View.VISIBLE;
        } else {
            tvPostDetailStatus.visibility = View.INVISIBLE;
        }
        tvPostDetailStatus.text = Utils.getString(v.context, "posts_status_${post.status}")

        tvPostDetailPrice.text = post.getPrice()

        if(post.expenses > 0) {
            tvPostDetailExpenses.visibility = View.VISIBLE
        } else {
            tvPostDetailExpenses.visibility = View.GONE
        }
        tvPostDetailExpenses.text = post.getExpenses()

        tvPostDetailTotalSize.text = resources.getString(R.string.post_detail_total).replace("{count}", building.surfaceOpen.toString())
        tvPostDetailCoveredSize.text = resources.getString(R.string.post_detail_covered).replace("{count}", building.surface.toString())
        tvPostDetailRooms.text = "${building.rooms} ${resources.getString(R.string.room)}${if(building.rooms > 1) "s" else ""}"
        tvPostDetailBathrooms.text = "${building.bathrooms} ${resources.getString(R.string.bathroom)}${if(building.bathrooms > 1) "s" else ""}"
        tvPostDetailBedRooms.text = "${building.bedrooms} ${resources.getString(R.string.bedroom)}${if(building.bedrooms > 1) "s" else ""}"
        tvPostDetailAge.text = "${building.antique.toString()} ${resources.getString(R.string.antique)}"

        tvPostDetailAddress.text = location.getFullAddress()
        tvPostDetailDistrict.text = location.district
        tvPostDetailCity.text = location.city

        mvPostDetailMap.onCreate(null)
        mvPostDetailMap.getMapAsync {
            MapsInitializer.initialize(v.context)
            setMapLocation(it, locationPosition)
        }
    }

    override fun onResume() {
        super.onResume()
        mvPostDetailMap.onResume();
    }

    override fun onPause() {
        super.onPause()
        mvPostDetailMap.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mvPostDetailMap.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mvPostDetailMap.onLowMemory()
    }

    fun setMapLocation(map : GoogleMap, position : LatLng) {
        with(map) {
            moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15f))
            addMarker(MarkerOptions().position(position))
            mapType=GoogleMap.MAP_TYPE_NORMAL
        }
    }
}