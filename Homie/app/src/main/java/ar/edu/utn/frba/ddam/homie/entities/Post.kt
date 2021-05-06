package ar.edu.utn.frba.ddam.homie.entities

import android.content.Context
import androidx.room.*
import ar.edu.utn.frba.ddam.homie.database.LocalDatabase
import ar.edu.utn.frba.ddam.homie.helpers.Utils
import java.util.*

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
    var dbId : String = ""
    @ColumnInfo(name = "building_id")
    var buildingId : Int
    var type : String
    var status : String
    var price : Int
    var expenses : Int
    var currency : String
    @ColumnInfo(name = "last_update")
    var lastUpdate : Date

    @Ignore
    constructor(id: Int, buildingId: Int, type: String, status : String, price: Int, expenses : Int, currency : String) {
        this.id = id
        this.dbId = Utils.generateHash(12)
        this.buildingId = buildingId
        this.type = type
        this.status = status
        this.buildingId = buildingId
        this.price = price
        this.expenses = expenses
        this.currency = currency
        this.lastUpdate = Date()
    }

    constructor(){
        this.type = ""
        this.status = ""
        this.buildingId = 0
        this.price = 0
        this.expenses = 0
        this.currency = ""
        this.lastUpdate = Date()
    }

    fun getBuilding(context : Context) : Building? {
        return LocalDatabase.getLocalDatabase(context)?.buildingDao()?.getById(buildingId);
    }

    fun getUserLike(context : Context, userId : Int) : Boolean {
        return (LocalDatabase.getLocalDatabase(context)?.postDao()?.getUserLikes(this.id, userId)!! > 0);
    }
}