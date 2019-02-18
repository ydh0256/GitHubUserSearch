package com.android.duckkite.githubusersearch.data.source.remote

import com.android.duckkite.githubusersearch.data.User
import com.google.gson.annotations.SerializedName

class UserSearchResponse(
        @SerializedName("total_count") val totalCount: Int,
        val items: List<User>)
