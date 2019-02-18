package com.android.duckkite.githubusersearch.ui.search

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.android.duckkite.githubusersearch.R
import org.koin.android.ext.android.inject
import androidx.databinding.DataBindingUtil
import com.android.duckkite.githubusersearch.databinding.SearchFragmentBinding
import com.android.duckkite.githubusersearch.ui.UserAdapter
import com.android.duckkite.githubusersearch.util.extention.hideKeyboard
import com.android.duckkite.githubusersearch.util.extention.plusAssign
import com.androidhuman.example.simplegithub.rx.AutoClearedDisposable
import com.jakewharton.rxbinding2.support.v7.widget.queryTextChangeEvents
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.search_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }
    private lateinit var viewDataBinding: SearchFragmentBinding
    internal val disposables = AutoClearedDisposable(this)
    internal val viewDisposables
            = AutoClearedDisposable(lifecycleOwner = this, alwaysClearOnStop = false)

    private val searchVewModel: SearchViewModel by viewModel{ parametersOf(this)}
    private val userAdapter: UserAdapter by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = SearchFragmentBinding.inflate(inflater, container, false).apply {
            viewmodel = searchVewModel
            setLifecycleOwner(this@SearchFragment)
        }
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpListView()
        lifecycle += disposables
        lifecycle += viewDisposables
        searchVewModel.disposables = disposables
        observeSearchText()
        searchVewModel.fetchFavoriteData()
    }

    fun setUpListView() {
        userAdapter.disposables = disposables
        listView.adapter = userAdapter
    }

    fun observeSearchText() {
        viewDisposables += searchView.queryTextChangeEvents()
            .filter { it.isSubmitted }
            .map { it.queryText() }
            .filter { it.isNotEmpty() }
            .map { it.toString() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { query ->
                searchVewModel.searchUser(query)
                hideKeyboard()
            }
    }

}
