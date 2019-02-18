package com.android.duckkite.githubusersearch.ui.favorite

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel;
import com.android.duckkite.githubusersearch.data.User
import com.android.duckkite.githubusersearch.data.source.UserDataSource
import com.android.duckkite.githubusersearch.util.extention.plusAssign
import com.androidhuman.example.simplegithub.rx.AutoActivatedDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FavoriteViewModel(
    val fragment: Fragment,
    val userDataSource: UserDataSource
) : ViewModel() {

    private val _searchItems = MutableLiveData<List<User>>().apply { value = emptyList() }
    val searchItems: LiveData<List<User>>
        get() = _searchItems
    val empty: LiveData<Boolean> = Transformations.map(_searchItems) {
        it.isEmpty()
    }

    private var originalList: List<User> = emptyList()
    private var searchKeyword = ""

    fun fetchFavoriteData() {
        fragment.lifecycle += AutoActivatedDisposable(fragment) {
            userDataSource.getUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ items ->
                    originalList = items
                    searchUser(searchKeyword)
                })
        }
    }


    fun searchUser(query: String) {
        searchKeyword = query
        if(searchKeyword.isEmpty()) {
            _searchItems.value = originalList
        } else {
            _searchItems.value = originalList.filter { it.login.contains(searchKeyword) }
        }
        mappingFavoriteData()
    }

    fun mappingFavoriteData() {
        searchItems.value?.forEach { user ->
            user.isFavorite = true
        }
    }

}
