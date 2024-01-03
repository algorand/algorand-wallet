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

package com.algorand.android.modules.walletconnect.domain.decider

import com.algorand.android.modules.walletconnect.client.v1.mapper.WalletConnectV1RequestIdentifierMapper
import com.algorand.android.modules.walletconnect.client.v2.mapper.WalletConnectV2RequestIdentifierMapper
import com.algorand.android.modules.walletconnect.domain.model.WalletConnect
import com.algorand.android.modules.walletconnect.domain.model.WalletConnectVersionIdentifier
import javax.inject.Inject

class WalletConnectRequestIdentifierDecider @Inject constructor(
    private val walletConnectV1RequestIdentifierMapper: WalletConnectV1RequestIdentifierMapper,
    private val walletConnectV2RequestIdentifierMapper: WalletConnectV2RequestIdentifierMapper
) {

    fun decideRequestIdentifier(
        requestId: Long,
        versionIdentifier: WalletConnectVersionIdentifier
    ): WalletConnect.RequestIdentifier {
        return when (versionIdentifier) {
            WalletConnectVersionIdentifier.VERSION_1 -> {
                walletConnectV1RequestIdentifierMapper.mapToRequestIdentifier(requestId)
            }
            WalletConnectVersionIdentifier.VERSION_2 -> {
                walletConnectV2RequestIdentifierMapper.mapToRequestIdentifier(requestId)
            }
        }
    }
}
