package ar.edu.utn.frba.ddam.homie.entities

import androidx.room.*
import ar.edu.utn.frba.ddam.homie.helpers.Utils
import com.google.firebase.Timestamp
import java.util.*
import kotlin.collections.HashMap

@Entity(tableName = "locations",
    indices = [
        Index(value = ["id"], unique = true),
        Index(value = ["db_id"])
    ]
)
class Location {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
    @ColumnInfo(name = "db_id")
    var dbId : String
    var city : String
    var district : String
    var address : String
    var number : Int
    var floor : Int
    var apartment : String
    var latitude : Double
    var longitude : Double
    @ColumnInfo(name = "last_update")
    var lastUpdate : Date

    @Ignore
    constructor(id : Int, dbId : String, city : String, district : String, address: String, number: Int, floor: Int, apartment: String, latitude : Double, longitude : Double) {
        this.id = id
        this.dbId = dbId
        this.city = city
        this.district = district
        this.address = address
        this.number = number
        this.floor = floor
        this.apartment = apartment
        this.latitude = latitude
        this.longitude = longitude
        this.lastUpdate = Date()
    }

    constructor() {
        this.dbId = ""
        this.city = ""
        this.district = ""
        this.address = ""
        this.number = 0
        this.floor = 0
        this.apartment = ""
        this.latitude = 0.0
        this.longitude = 0.0
        this.lastUpdate = Date()
    }

    fun getFullAddress() : String {
        return "$address ${number.toString()}"
    }

    fun getLocationCloud() : LocationCloud {
        return LocationCloud(dbId, city, district, address, number, floor, apartment, latitude, longitude, lastUpdate)
    }

    class LocationCloud(
        var id : String,
        var city : String,
        var district : String,
        var address : String,
        var number : Int,
        var floor : Int,
        var apartment : String,
        var latitude : Double,
        var longitude : Double,
        var last_update : Date
    ) {
        constructor(data : Map<String, Any>) : this(
                data["id"] as String,
                data["city"] as String,
                data["district"] as String,
                data["address"] as String,
                (data["number"] as Long).toInt(),
                (data["floor"] as Long).toInt(),
                data["apartment"] as String,
                data["latitude"] as Double,
                data["longitude"] as Double,
                (data["last_update"] as Timestamp).toDate())
    }
}