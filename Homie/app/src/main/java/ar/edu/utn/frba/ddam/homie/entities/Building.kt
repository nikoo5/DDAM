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
    var dbId : String = ""
    @ColumnInfo(name = "location_id")
    var locationId : Int
    var type : String
    var surface : Long
    var surfaceOpen : Long
    var rooms : Int
    var features : MutableList<String> = mutableListOf()
    var images : MutableList<String> = mutableListOf()
    @ColumnInfo(name = "last_update")
    var lastUpdate : Date

    @Ignore
    constructor(id: Int, locationId: Int, type: String, surface: Long, surfaceOpen: Long, rooms: Int) {
        this.id = id
        this.dbId = Utils.generateHash(12)
        this.type = type
        this.locationId = locationId
        this.surface = surface
        this.surfaceOpen = surfaceOpen
        this.rooms = rooms
        this.lastUpdate = Date()
    }

    constructor() {
        this.type = ""
        this.locationId = 0
        this.surface = 0
        this.surfaceOpen = 0
        this.rooms = 0
        this.features = mutableListOf()
        this.lastUpdate = Date()
    }

    fun getLocation(context : Context) : Location? {
        return LocalDatabase.getLocalDatabase(context)?.locationDao()?.getById(locationId);
    }
}
