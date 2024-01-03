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

package com.algorand.android.modules.walletconnect.domain

import android.app.Application
import com.algorand.android.modules.walletconnect.domain.model.WalletConnect
import com.algorand.android.modules.walletconnect.domain.model.WalletConnect.ChainIdentifier
import com.algorand.android.modules.walletconnect.domain.model.WalletConnect.RequestIdentifier
import com.algorand.android.modules.walletconnect.domain.model.WalletConnect.SessionIdentifier
import com.algorand.android.modules.walletconnect.domain.model.WalletConnectBlockchain
import com.algorand.android.modules.walletconnect.domain.model.WalletConnectClientListener
import com.algorand.android.modules.walletconnect.domain.model.WalletConnectError

interface WalletConnectClient {

    fun setListener(listener: WalletConnectClientListener)

    fun connect(uri: String)
    suspend fun connect(sessionIdentifier: SessionIdentifier)

    // Session callbacks
    suspend fun approveSession(
        proposalIdentifier: WalletConnect.Session.ProposalIdentifier,
        requiredNamespaces: Map<WalletConnectBlockchain, WalletConnect.Namespace.Proposal>,
        accountAddresses: List<String>
    )

    suspend fun updateSession(
        sessionIdentifier: SessionIdentifier,
        accountAddresses: List<String>,
        removedAccountAddress: String?
    )

    suspend fun rejectSession(proposalIdentifier: WalletConnect.Session.ProposalIdentifier, reason: String)

    // Request callbacks
    suspend fun rejectRequest(
        sessionIdentifier: SessionIdentifier,
        requestIdentifier: RequestIdentifier,
        errorResponse: WalletConnectError
    )

    suspend fun approveRequest(
        sessionIdentifier: SessionIdentifier,
        requestIdentifier: RequestIdentifier,
        payload: Any,
    )

    fun getDefaultChainIdentifier(): ChainIdentifier

    fun isValidSessionUrl(url: String): Boolean

    suspend fun killSession(sessionIdentifier: SessionIdentifier)

    suspend fun getWalletConnectSession(sessionIdentifier: SessionIdentifier): WalletConnect.SessionDetail?
    suspend fun getAllWalletConnectSessions(): List<WalletConnect.SessionDetail>
    suspend fun getSessionsByAccountAddress(accountAddress: String): List<WalletConnect.SessionDetail>
    suspend fun getDisconnectedWalletConnectSessions(): List<WalletConnect.SessionDetail>
    suspend fun setAllSessionsDisconnected()

    suspend fun disconnectFromAllSessions()
    suspend fun connectToDisconnectedSessions()

    suspend fun initializeClient(application: Application)
}
