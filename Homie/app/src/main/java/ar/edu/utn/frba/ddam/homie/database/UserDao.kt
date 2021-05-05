package ar.edu.utn.frba.ddam.homie.database

import androidx.room.*
import ar.edu.utn.frba.ddam.homie.entities.User

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :id")
    fun getById(id : Int) : User?

    @Query("SELECT * FROM users WHERE db_id = :db_id")
    fun getByDbId(db_id : String) : User?

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