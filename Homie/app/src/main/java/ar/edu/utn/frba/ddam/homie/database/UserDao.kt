package ar.edu.utn.frba.ddam.homie.database

import androidx.room.*
import ar.edu.utn.frba.ddam.homie.entities.User

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :id")
    fun getById(id : Int) : User?

    @Query("SELECT * FROM users WHERE db_id = :db_id")
    fun getByDbId(db_id : String) : User?

    @Query("SELECT COUNT(p.id) FROM posts p JOIN users_posts up ON up.post_id = p.id JOIN users u ON u.id = up.user_id WHERE u.id = :id")
    fun getPostsCount(id : Int) : Int

    @Query("SELECT 0 WHERE :id = :id")
    fun getCommentsCount(id : Int) : Int

    @Query("SELECT 0 WHERE :id = :id")
    fun getFriendsCount(id : Int) : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User?) : Long

    @Update
    fun update(user : User?)

    @Delete
    fun delete(user : User?)

    @Query("DELETE FROM users")
    fun clearTable()

    @Query("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='users'")
    fun resetTable()
}