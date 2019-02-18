/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.duckkite.githubusersearch.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import android.widget.ListView
import com.android.duckkite.githubusersearch.R
import com.android.duckkite.githubusersearch.data.User
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * Contains [BindingAdapter]s for the [Task] list.
 */
object UserListBindings {

    @BindingAdapter("app:items")
    @JvmStatic fun setItems(listView: ListView, items: List<User>) {
        with(listView.adapter as UserAdapter) {
            replaceData(items)
        }
    }

    @BindingAdapter("app:imageUrl")
    @JvmStatic fun loadImage(imageView: ImageView, imageUrl: String) {
        Glide.with(imageView.context)
            .load(imageUrl)
            .apply(RequestOptions().apply(){
                centerCrop()
                placeholder(R.drawable.ic_person)
            })
            .into(imageView)
    }
}
