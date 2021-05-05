package ar.edu.utn.frba.ddam.homie.database

import androidx.room.*
import ar.edu.utn.frba.ddam.homie.entities.Building

@Dao
interface BuildingDao {
    @Query("SELECT * FROM buildings ORDER BY id LIMIT :limit OFFSET :offset")
    fun getAll(limit : Int = 999999999, offset : Int = 0) : MutableList<Building>

    @Query("SELECT * FROM buildings WHERE id = :id")
    fun getById(id : Int) : Building?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(building: Building?) : Long

    @Update
    fun update(building: Building?)

    @Delete
    fun delete(building: Building?)

    @Query("DELETE FROM buildings")
    fun clearTable()

    @Query("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='buildings'")
    fun resetTable()
}