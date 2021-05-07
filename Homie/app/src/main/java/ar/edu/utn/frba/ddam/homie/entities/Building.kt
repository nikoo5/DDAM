package ar.edu.utn.frba.ddam.homie.entities

import android.content.Context
import androidx.room.*
import ar.edu.utn.frba.ddam.homie.database.LocalDatabase
import ar.edu.utn.frba.ddam.homie.helpers.Utils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

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
}
