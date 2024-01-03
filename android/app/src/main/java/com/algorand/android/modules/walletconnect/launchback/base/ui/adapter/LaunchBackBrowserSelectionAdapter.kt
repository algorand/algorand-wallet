/*
 * Copyright 2022 Pera Wallet, LDA
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.algorand.android.modules.walletconnect.launchback.multiplebrowser.base.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.algorand.android.models.BaseDiffUtil
import com.algorand.android.models.BaseViewHolder
import com.algorand.android.modules.walletconnect.launchback.base.ui.model.LaunchBackBrowserListItem
import com.algorand.android.modules.walletconnect.launchback.multiplebrowser.base.ui.adapter.viewholder.LaunchBackBrowserItemViewHolder

class LaunchBackBrowserSelectionAdapter(
    private val listener: Listener
) : ListAdapter<LaunchBackBrowserListItem, BaseViewHolder<LaunchBackBrowserListItem>>(BaseDiffUtil()) {

    private val launchBackBrowserItemViewHolderListener = LaunchBackBrowserItemViewHolder.Listener { packageName ->
        listener.onBrowserSelected(packageName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaunchBackBrowserItemViewHolder {
        return LaunchBackBrowserItemViewHolder.create(parent, launchBackBrowserItemViewHolderListener)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<LaunchBackBrowserListItem>, position: Int) {
        holder.bind(getItem(position))
    }

    fun interface Listener {
        fun onBrowserSelected(packageName: String)
    }
}
