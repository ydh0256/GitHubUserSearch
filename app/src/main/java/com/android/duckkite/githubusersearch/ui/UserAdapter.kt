/*
 *  Copyright 2017 Google Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.android.duckkite.githubusersearch.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.RadioGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import com.android.duckkite.githubusersearch.data.User
import com.android.duckkite.githubusersearch.data.source.UserDataSource
import com.android.duckkite.githubusersearch.databinding.UserItemBinding
import com.android.duckkite.githubusersearch.util.extention.plusAssign
import com.android.duckkite.githubusersearch.util.extention.runOnIoScheduler
import com.androidhuman.example.simplegithub.rx.AutoClearedDisposable
import io.reactivex.disposables.Disposable

class UserAdapter(
    val userDataSource: UserDataSource
): BaseAdapter() {

    private var users: MutableList<User> = mutableListOf()
    var disposables: AutoClearedDisposable? = null

    fun replaceData(tasks: List<User>) {
        setList(tasks)
    }

    override fun getCount() = users.size

    override fun getItem(position: Int) = users[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup): View {
        val binding: UserItemBinding
        binding = if (view == null) {
            // Inflate
            val inflater = LayoutInflater.from(viewGroup.context)

            // Create the binding
            UserItemBinding.inflate(inflater, viewGroup, false)
        } else {
            // Recycling view
            DataBindingUtil.getBinding(view) ?: throw IllegalStateException()
        }

        with(binding) {
            user = users[position]
            executePendingBindings()
        }

        binding.checkBox.setOnClickListener() {
            disposables?.let {
                it += runOnIoScheduler{
                    if(!users[position].isFavorite){
                        userDataSource.addUser(users[position])
                    } else {
                        userDataSource.removeUser(users[position])
                    }
                }
            }
        }

        return binding.root
    }


    private fun setList(users: List<User>) {
        this.users.clear()
        this.users.addAll(users)
        notifyDataSetChanged()
    }
}
