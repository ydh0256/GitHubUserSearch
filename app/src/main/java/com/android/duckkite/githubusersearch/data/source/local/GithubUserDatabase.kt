package com.android.duckkite.githubusersearch.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.duckkite.githubusersearch.data.User

@Database(entities = arrayOf(User::class), version = 1)
abstract class GithubUserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
