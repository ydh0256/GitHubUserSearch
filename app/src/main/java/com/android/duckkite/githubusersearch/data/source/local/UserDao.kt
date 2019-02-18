package com.android.duckkite.githubusersearch.data.source.local


import androidx.room.*
import com.android.duckkite.githubusersearch.data.User
import io.reactivex.Flowable

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(user: User)

    @Delete
    fun delete(user: User)

    @Query("SELECT * FROM users")
    fun getUsers(): Flowable<List<User>>

    @Query("DELETE FROM users")
    fun clearAll()
}
