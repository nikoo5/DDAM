package ar.edu.utn.frba.ddam.homie.entities

import android.content.Context
import androidx.room.*
import ar.edu.utn.frba.ddam.homie.database.LocalDatabase
import ar.edu.utn.frba.ddam.homie.helpers.Utils
import com.google.firebase.Timestamp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import kotlin.collections.HashMap

@Entity(tableName = "buildings",
    indices = [
        Index(value = ["id"], unique = true),
        Index(value = ["db_id"]),
        Index(value = ["location_id"])
    ],
    foreignKeys = [
        ForeignKey(entity = Location::class, parentColumns = ["id"], childColumns = ["location_id"])
    ]
)
class Building {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
    @ColumnInfo(name = "db_id")
    var dbId : String
    @ColumnInfo(name = "location_id")
    var locationId : Int
    var type : String
    var surface : Long
    var surfaceOpen : Long
    var rooms : Int
    var bathrooms : Int
    var bedrooms : Int
    var antique : Int
    var features : MutableList<String> = mutableListOf()
    var images : MutableList<String> = mutableListOf()
    @ColumnInfo(name = "last_update")
    var lastUpdate : Date

    @Ignore
    constructor(id: Int, dbId : String, locationId: Int, type: String, surface: Long, surfaceOpen: Long, rooms: Int, bathrooms: Int, bedrooms: Int, antique: Int) {
        this.id = id
        this.dbId = dbId
        this.type = type
        this.locationId = locationId
        this.surface = surface
        this.surfaceOpen = surfaceOpen
        this.rooms = rooms
        this.bathrooms = bathrooms
        this.bedrooms = bedrooms
        this.antique = antique
        this.lastUpdate = Date()
    }

    constructor() {
        this.dbId = ""
        this.type = ""
        this.locationId = 0
        this.surface = 0
        this.surfaceOpen = 0
        this.rooms = 0
        this.bathrooms = 0
        this.bedrooms = 0
        this.antique = 0
        this.lastUpdate = Date()
    }

    fun getLocation(context : Context) : Location? {
        return LocalDatabase.getLocalDatabase(context)?.locationDao()?.getById(locationId);
    }

    fun getBuildingCloud(context : Context) : BuildingCloud {
        val location = getLocation(context)!!
        return BuildingCloud(dbId, location.getLocationCloud(), type, surface, surfaceOpen, rooms, bathrooms, bedrooms, antique, features, images, lastUpdate)
    }

    class BuildingCloud(
        var id : String,
        var location : Location.LocationCloud,
        var type : String,
        var surface : Long,
        var surface_open : Long,
        var rooms : Int,
        var bathrooms : Int,
        var bedrooms : Int,
        var antique : Int,
        var features : MutableList<String>,
        var images : MutableList<String>,
        var last_update : Date
    ) {
        constructor(data : Map<String, Any>) : this(
                data["id"] as String,
                Location.LocationCloud(data["location"] as Map<String, Any>),
                data["type"] as String,
                data["surface"] as Long,
                data["surface_open"] as Long,
                (data["rooms"] as Long).toInt(),
                (data["bathrooms"] as Long).toInt(),
                (data["bedrooms"] as Long).toInt(),
                (data["antique"] as Long).toInt(),
                data["features"] as MutableList<String>,
                data["images"] as MutableList<String>,
                (data["last_update"] as Timestamp).toDate())
    }
}
