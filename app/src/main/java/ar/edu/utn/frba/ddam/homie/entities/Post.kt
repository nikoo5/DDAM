package ar.edu.utn.frba.ddam.homie.entities

import android.content.Context
import androidx.room.*
import ar.edu.utn.frba.ddam.homie.database.LocalDatabase
import ar.edu.utn.frba.ddam.homie.helpers.Utils
import com.google.firebase.Timestamp
import java.text.NumberFormat
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.exp

@Entity(tableName = "posts",
    indices = [
        Index(value = ["id"], unique = true),
        Index(value = ["db_id"]),
        Index(value = ["building_id"])
    ],
    foreignKeys = [
        ForeignKey(entity = Building::class, parentColumns = ["id"], childColumns = ["building_id"])
    ]
)
class Post {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
    @ColumnInfo(name = "db_id")
    var dbId : String
    var document : String = ""
    @ColumnInfo(name = "building_id")
    var buildingId : Int
    var type : String
    var status : String
    var price : Int
    var expenses : Int
    var currency : String
    @ColumnInfo(name = "view_count")
    var viewCount : Int = 0
    @ColumnInfo(name = "view_count_relative")
    var viewCountRelative : Int = 0
    @ColumnInfo(name = "contact_phone")
    var contactPhone : String
    @ColumnInfo(name = "last_update")
    var lastUpdate : Date

    @Ignore
    constructor(id: Int, dbId : String, buildingId: Int, type: String, status : String, price: Int, expenses : Int, currency : String, contactPhone : String) {
        this.id = id
        this.dbId = dbId
        this.buildingId = buildingId
        this.type = type
        this.status = status
        this.buildingId = buildingId
        this.price = price
        this.expenses = expenses
        this.currency = currency
        this.contactPhone = contactPhone
        this.lastUpdate = Date()
    }

    constructor(){
        this.dbId = ""
        this.document = ""
        this.type = ""
        this.status = ""
        this.buildingId = 0
        this.price = 0
        this.expenses = 0
        this.currency = ""
        this.contactPhone = ""
        this.lastUpdate = Date()
    }

    fun getPrice() : String {
        return "$currency ${NumberFormat.getIntegerInstance().format(price).replace(",", ".")}"
    }

    fun getExpenses() : String {
        return "$currency ${NumberFormat.getIntegerInstance().format(expenses).replace(",", ".")}"
    }

    fun getBuilding(context : Context) : Building? {
        return LocalDatabase.getLocalDatabase(context)?.buildingDao()?.getById(buildingId);
    }

    fun getUserLike(context : Context, userId : Int) : Boolean {
        return (LocalDatabase.getLocalDatabase(context)?.postDao()?.getUserLikes(this.id, userId)!! > 0);
    }

    fun getPostCloud(context: Context) : PostCloud {
        val building = getBuilding(context)!!
        return PostCloud(dbId, building.getBuildingCloud(context), type, status, price, expenses, currency, viewCount, contactPhone, lastUpdate)
    }

    class PostCloud(
        var id : String,
        var building : Building.BuildingCloud,
        var type : String,
        var status : String,
        var price : Int,
        var expenses : Int,
        var currency : String,
        var view_count : Int,
        var contact_phone : String,
        var last_update : Date
    ) {
        constructor(data : Map<String, Any>) : this(
                data["id"] as String,
                Building.BuildingCloud(data["building"] as Map<String, Any>),
                data["type"] as String,
                data["status"] as String,
                (data["price"] as Long).toInt(),
                (data["expenses"] as Long).toInt(),
                data["currency"] as String,
                (data["view_count"] as Long).toInt(),
                data["contact_phone"] as String,
                (data["last_update"] as Timestamp).toDate())
    }
}