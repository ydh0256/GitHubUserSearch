package com.android.duckkite.githubusersearch.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: Int,
    val login: String,
    @SerializedName("avatar_url") @ColumnInfo(name = "avatar_url") val avatarUrl: String,
    var isFavorite: Boolean = false
)