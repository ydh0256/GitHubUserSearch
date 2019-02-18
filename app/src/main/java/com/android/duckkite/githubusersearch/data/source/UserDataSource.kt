package com.android.duckkite.githubusersearch.data.source

import com.android.duckkite.githubusersearch.data.User
import com.android.duckkite.githubusersearch.data.source.remote.UserSearchResponse
import io.reactivex.Flowable
import io.reactivex.Observable
import java.util.*

interface UserDataSource {

    fun searchUsers(searchKeyword: String): Observable<UserSearchResponse>
    fun getUsers(): Flowable<List<User>>
    fun addUser(user: User)
    fun removeUser(user: User)
}