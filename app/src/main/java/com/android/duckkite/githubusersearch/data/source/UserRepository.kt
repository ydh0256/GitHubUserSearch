package com.android.duckkite.githubusersearch.data.source

import com.android.duckkite.githubusersearch.data.User
import com.android.duckkite.githubusersearch.data.source.local.UserDao
import com.android.duckkite.githubusersearch.data.source.remote.GithubApi
import com.android.duckkite.githubusersearch.data.source.remote.UserSearchResponse
import io.reactivex.Flowable
import io.reactivex.Observable

class UserRepository(
    val githubApi: GithubApi,
    val userDao: UserDao
): UserDataSource {
    override fun searchUsers(searchKeyword: String): Observable<UserSearchResponse> {
        return githubApi.searchUsers(searchKeyword)
    }

    override fun getUsers(): Flowable<List<User>> {
        return userDao.getUsers()
    }

    override fun addUser(user: User) {
        userDao.add(user)
    }

    override fun removeUser(user: User) {
        userDao.delete(user)
    }
}