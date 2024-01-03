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

package com.algorand.android.modules.walletconnect.connectedapps.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.algorand.android.models.BaseDiffUtil
import com.algorand.android.models.BaseViewHolder
import com.algorand.android.modules.walletconnect.connectedapps.ui.model.WalletConnectSessionItem
import com.algorand.android.modules.walletconnect.ui.model.WalletConnectSessionIdentifier

class WalletConnectSessionAdapter(
    private val listener: WalletConnectSessionAdapterListener
) : ListAdapter<WalletConnectSessionItem, BaseViewHolder<WalletConnectSessionItem>>(BaseDiffUtil()) {

    private val walletConnectSessionViewHolderListener = object : WalletConnectSessionViewHolder.Listener {

        override fun onSessionDisconnectClick(sessionIdentifier: WalletConnectSessionIdentifier) {
            listener.onDisconnectClick(sessionIdentifier)
        }

        override fun onSessionClick(sessionIdentifier: WalletConnectSessionIdentifier) {
            listener.onSessionClick(sessionIdentifier)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<WalletConnectSessionItem> {
        return WalletConnectSessionViewHolder.create(parent, walletConnectSessionViewHolderListener)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<WalletConnectSessionItem>, position: Int) {
        holder.bind(getItem(position))
    }

    interface WalletConnectSessionAdapterListener {
        fun onDisconnectClick(sessionIdentifier: WalletConnectSessionIdentifier)
        fun onSessionClick(sessionIdentifier: WalletConnectSessionIdentifier)
    }
}
