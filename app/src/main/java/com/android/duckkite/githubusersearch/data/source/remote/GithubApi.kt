package com.android.duckkite.githubusersearch.data.source.remote

import com.android.duckkite.githubusersearch.data.User
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApi {
    @GET("search/users")
    fun searchUsers(@Query("q") query: String): Observable<UserSearchResponse>
}
