package ar.edu.utn.frba.ddam.homie.database

import androidx.room.*
import ar.edu.utn.frba.ddam.homie.entities.UserPosts

@Dao
interface UserPostsDao {
    @Query("SELECT * FROM users_posts")
    fun getAll() : MutableList<UserPosts>?

    @Query("SELECT * FROM users_posts WHERE id = :id")
    fun getById(id : Int) : UserPosts?

    @Query("SELECT * FROM users_posts WHERE user_id = :userId")
    fun getByUserId(userId : Int) : MutableList<UserPosts>?

    @Query("SELECT * FROM users_posts WHERE user_id = :userId AND post_id = :postId")
    fun getByBothId(userId : Int, postId : Int) : UserPosts?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userPost: UserPosts?) : Long

    @Update
    fun update(userPost : UserPosts?)

    @Delete
    fun delete(userPost : UserPosts?)

    @Query("DELETE FROM users_posts")
    fun clearTable()

    @Query("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='users_posts'")
    fun resetTable()
}