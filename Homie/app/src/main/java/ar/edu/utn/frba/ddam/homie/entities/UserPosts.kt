package ar.edu.utn.frba.ddam.homie.entities

import androidx.room.*
import java.util.*

@Entity(tableName = "users_posts",
        indices = [
            Index(value = ["id"], unique = true),
            Index(value = ["user_id"]),
            Index(value = ["post_id"]),
            Index(value = ["user_id", "post_id"], unique = true),
            Index(value = ["db_id"])
        ],
        foreignKeys = [
            ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["user_id"]),
            ForeignKey(entity = Post::class, parentColumns = ["id"], childColumns = ["post_id"])
        ]
)
class UserPosts {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
    @ColumnInfo(name = "db_id")
    var dbId : String = ""
    @ColumnInfo(name = "user_id")
    var userId : Int
    @ColumnInfo(name = "post_id")
    var postId : Int
    var date : Date

    @Ignore
    constructor(userId : Int, postId : Int) {
        this.userId = userId
        this.postId = postId
        this.date = Date()
    }

    constructor() {
        this.userId = 0
        this.postId = 0
        this.date = Date()
    }
}