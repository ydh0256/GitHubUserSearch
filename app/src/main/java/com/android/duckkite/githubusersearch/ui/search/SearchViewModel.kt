package com.android.duckkite.githubusersearch.ui.search

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel;
import com.android.duckkite.githubusersearch.data.User
import com.android.duckkite.githubusersearch.data.source.UserDataSource
import com.android.duckkite.githubusersearch.util.extention.plusAssign
import com.androidhuman.example.simplegithub.rx.AutoActivatedDisposable
import com.androidhuman.example.simplegithub.rx.AutoClearedDisposable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SearchViewModel(
    val fragment: Fragment,
    val userDataSource: UserDataSource
) : ViewModel() {

    var disposables: AutoClearedDisposable? = null

    private val _searchItems = MutableLiveData<List<User>>().apply { value = emptyList() }
    val searchItems: LiveData<List<User>>
        get() = _searchItems

    private val _isShowProgress = MutableLiveData<Boolean>().apply { value = false }
    val isShowProgress: LiveData<Boolean>
        get() = _isShowProgress

    private val _errorMessage = MutableLiveData<String>().apply { value = "Github의 유저 아이디를 검색합니다." }
    val errorMessage: LiveData<String>
        get() = _errorMessage

    val empty: LiveData<Boolean> = Transformations.map(_searchItems) {
        it.isEmpty()
    }
    var searchItemSet : Set<User> = emptySet()

    fun fetchFavoriteData() {
        fragment.lifecycle += AutoActivatedDisposable(fragment){
            userDataSource.getUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ items ->
                    searchItemSet = items.toHashSet()
                    mappingFavoriteData()
                })
        }
    }


    fun searchUser(query: String) {
        disposables?.let {
            it += userDataSource.searchUsers(query)
                .flatMap {
                    if (0 == it.totalCount) {
                        Observable.error(IllegalStateException("검색결과가 없습니다."))
                    } else {
                        Observable.just(it.items)
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    clearError()
                    showProgress()
                }
                .doOnTerminate { hideProgress() }
                .subscribe({ items ->
                    _searchItems.value = items
                    mappingFavoriteData()
                }) {
                    _searchItems.value = emptyList()
                    showError(it.message)
                }
        }
    }

    fun mappingFavoriteData() {
        searchItems.value?.forEach { user ->
            user.isFavorite = searchItemSet.contains(user)
        }
    }

    fun showProgress() {
        _isShowProgress.value = true
    }
    fun hideProgress() {
        _isShowProgress.value = false
    }

    fun clearError() {
        _errorMessage.value = ""
    }

    fun showError(message: String?) {
        message.let {
            _errorMessage.value = it
        }
    }
}
