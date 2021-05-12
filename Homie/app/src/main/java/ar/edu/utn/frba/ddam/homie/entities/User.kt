package ar.edu.utn.frba.ddam.homie.entities

import android.content.Context
import androidx.room.*
import ar.edu.utn.frba.ddam.homie.database.LocalDatabase
import java.util.*

@Entity(tableName = "users",
        indices = [
            Index(value = ["id"], unique = true),
            Index(value = ["db_id"])
        ]
)
class User {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
    @ColumnInfo(name = "db_id")
    var dbId : String = ""
    var name : String
    @ColumnInfo(name = "last_name")
    var lastName : String
    var email : String
    var image : String
    @ColumnInfo(name = "last_login")
    var lastLogin : Date
    @ColumnInfo(name = "last_modification")
    var lastModification : Date

    @Ignore
    constructor(dbId : String, name : String, lastName : String, email : String, image : String) {
        this.id = 0;
        this.dbId = dbId
        this.name = name
        this.lastName = lastName
        this.email = email
        this.image = image
        this.lastLogin = Date()
        this.lastModification = Date()
    }

    constructor() {
        this.name = "";
        this.lastName = "";
        this.email = ""
        this.image = ""
        this.lastLogin = Date()
        this.lastModification = Date()
    }

    fun getDisplayName() : String {
        return "${this.name} ${this.lastName}"
    }

    fun getLikePosts(context : Context) : MutableList<Post> {
        return LocalDatabase.getLocalDatabase(context)?.postDao()?.getByUserId(id)!!
    }

    fun getLikesCount(context: Context) : Int {
        return LocalDatabase.getLocalDatabase(context)?.userDao()?.getPostsCount(id)!!
    }

    fun getCommentsCount(context: Context) : Int {
        return LocalDatabase.getLocalDatabase(context)?.userDao()?.getCommentsCount(id)!!
    }

    fun getFriendsCount(context: Context) : Int {
        return LocalDatabase.getLocalDatabase(context)?.userDao()?.getFriendsCount(id)!!
    }

    fun getUserCloud() : UserCloud {
        return UserCloud(dbId, name, lastName,email, image, lastLogin, lastModification, mutableListOf())
    }

    class UserCloud(
        var id : String,
        var name : String,
        var last_name : String,
        var email : String,
        var image : String,
        var last_login : Date,
        var last_modification : Date,
        var likes : MutableList<String>
    ) { }
}