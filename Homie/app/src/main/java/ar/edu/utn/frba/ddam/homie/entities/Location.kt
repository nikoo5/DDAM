package ar.edu.utn.frba.ddam.homie.entities

import androidx.room.*
import ar.edu.utn.frba.ddam.homie.helpers.Utils
import com.google.firebase.database.IgnoreExtraProperties
import java.util.*

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
    var dbId : String = ""
    var city : String
    var district : String
    var address : String
    var number : Int
    var floor : Int
    var apartment : String
    var latitude : String
    var longitude : String
    @ColumnInfo(name = "last_update")
    var lastUpdate : Date

    @Ignore
    constructor(id : Int, city : String, district : String, address: String, number: Int, floor: Int, apartment: String, latitude : String, longitude : String) {
        this.id = id
        this.dbId = Utils.generateHash(12)
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
        this.city = ""
        this.district = ""
        this.address = ""
        this.number = 0
        this.floor = 0
        this.apartment = ""
        this.latitude = ""
        this.longitude = ""
        this.lastUpdate = Date()
    }
}