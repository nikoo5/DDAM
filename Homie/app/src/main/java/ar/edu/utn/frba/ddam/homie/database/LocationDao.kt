package ar.edu.utn.frba.ddam.homie.database

import androidx.room.*
import ar.edu.utn.frba.ddam.homie.entities.Building
import ar.edu.utn.frba.ddam.homie.entities.Location
import ar.edu.utn.frba.ddam.homie.entities.Post

@Dao
interface LocationDao {
    @Query("SELECT * FROM locations ORDER BY id LIMIT :limit OFFSET :offset")
    fun getAll(limit : Int = 999999999, offset : Int = 0) : MutableList<Location>

    @Query("SELECT * FROM locations WHERE id = :id")
    fun getById(id : Int) : Location?

    @Query("SELECT * FROM locations WHERE db_id = :dbId")
    fun getByDbId(dbId : String) : Location?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(location: Location?) : Long

    @Update
    fun update(location: Location?)

    @Delete
    fun delete(location: Location?)

    @Query("DELETE FROM locations")
    fun clearTable()

    @Query("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='locations'")
    fun resetTable()
}