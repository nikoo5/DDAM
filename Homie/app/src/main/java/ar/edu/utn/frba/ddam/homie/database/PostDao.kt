package ar.edu.utn.frba.ddam.homie.database

import androidx.room.*
import ar.edu.utn.frba.ddam.homie.entities.Location
import ar.edu.utn.frba.ddam.homie.entities.Post

@Dao
interface PostDao {
    @Query("SELECT * FROM posts ORDER BY id LIMIT :limit OFFSET :offset")
    fun getAll(limit : Int = 999999999, offset : Int = 0) : MutableList<Post>

    @Query("SELECT * FROM posts WHERE id = :id")
    fun getById(id : Int) : Post?

    @Query("SELECT p.* FROM posts p JOIN users_posts up ON up.post_id = p.id JOIN users u ON u.id = up.user_id WHERE u.id = :id")
    fun getByUserId(id : Int) : MutableList<Post>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: Post?) : Long

    @Update
    fun update(post: Post?)

    @Delete
    fun delete(post: Post?)

    @Query("DELETE FROM posts")
    fun clearTable()

    @Query("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='posts'")
    fun resetTable()
}