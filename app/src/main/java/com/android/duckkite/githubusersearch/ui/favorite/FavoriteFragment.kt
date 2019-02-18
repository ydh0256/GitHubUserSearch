package com.android.duckkite.githubusersearch.ui.favorite

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.android.duckkite.githubusersearch.R
import com.android.duckkite.githubusersearch.databinding.FavoriteFragmentBinding
import com.android.duckkite.githubusersearch.ui.UserAdapter
import com.android.duckkite.githubusersearch.util.extention.hideKeyboard
import com.android.duckkite.githubusersearch.util.extention.plusAssign
import com.androidhuman.example.simplegithub.rx.AutoClearedDisposable
import com.jakewharton.rxbinding2.support.v7.widget.queryTextChangeEvents
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.search_fragment.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.concurrent.TimeUnit

class FavoriteFragment : Fragment() {

    companion object {
        fun newInstance() = FavoriteFragment()
    }

    private lateinit var viewDataBinding: FavoriteFragmentBinding
    internal val disposables = AutoClearedDisposable(this)
    internal val viewDisposables
            = AutoClearedDisposable(lifecycleOwner = this, alwaysClearOnStop = false)

    private val favoriteVewModel: FavoriteViewModel by viewModel{ parametersOf(this)}
    private val userAdapter: UserAdapter by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FavoriteFragmentBinding.inflate(inflater, container, false).apply {
            viewmodel = favoriteVewModel
            setLifecycleOwner(this@FavoriteFragment)
        }
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpListView()
        lifecycle += disposables
        lifecycle += viewDisposables
        observeSearchText()
        favoriteVewModel.fetchFavoriteData()
    }

    fun setUpListView() {
        userAdapter.disposables = disposables
        listView.adapter = userAdapter
    }

    fun observeSearchText() {
        viewDisposables += searchView.queryTextChangeEvents()
            .throttleFirst(100, TimeUnit.MILLISECONDS)
            .map { it.queryText() }
            .map { it.toString() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { query ->
                favoriteVewModel.searchUser(query)
            }
    }

}
