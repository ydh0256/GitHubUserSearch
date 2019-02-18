package com.android.duckkite.githubusersearch.di

import androidx.fragment.app.Fragment
import androidx.room.Room
import com.android.duckkite.githubusersearch.data.source.UserDataSource
import com.android.duckkite.githubusersearch.data.source.UserRepository
import com.android.duckkite.githubusersearch.data.source.local.GithubUserDatabase
import com.android.duckkite.githubusersearch.data.source.remote.GithubApi
import com.android.duckkite.githubusersearch.ui.UserAdapter
import com.android.duckkite.githubusersearch.ui.favorite.FavoriteViewModel
import com.android.duckkite.githubusersearch.ui.search.SearchViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.Module
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val repositoryModule: Module = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(provideOkHttpClient(provideLoggingInterceptor()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GithubApi::class.java)
    }
    single {
        Room.databaseBuilder(androidApplication(),
            GithubUserDatabase::class.java, "github.db")
            .build().userDao()
    }

    single<UserDataSource> { UserRepository(get(), get()) }
}

val viewModelModule: Module = module {
    viewModel {(fragment: Fragment)-> SearchViewModel(fragment, get()) }
    viewModel {(fragment: Fragment)-> FavoriteViewModel(fragment, get()) }

}

val adapterModule: Module = module {
    factory { UserAdapter(get()) }
}

val appModules = listOf(repositoryModule, viewModelModule, adapterModule)

private fun provideOkHttpClient(
    interceptor: HttpLoggingInterceptor): OkHttpClient
        = OkHttpClient.Builder()
    .run {
        addInterceptor(interceptor)
        build()
    }

private fun provideLoggingInterceptor(): HttpLoggingInterceptor
        = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

