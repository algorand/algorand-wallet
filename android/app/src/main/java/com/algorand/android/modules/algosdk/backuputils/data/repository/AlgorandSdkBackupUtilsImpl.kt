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

package com.algorand.android.modules.algosdk.backuputils.data.repository

import com.algorand.algosdk.sdk.Sdk
import com.algorand.android.modules.algosdk.backuputils.domain.repository.AlgorandSdkBackupUtils

class AlgorandSdkBackupUtilsImpl : AlgorandSdkBackupUtils {

    override suspend fun generateBackupKey(): ByteArray? {
        return try {
            Sdk.generateBackupPrivateKey()
        } catch (exception: Exception) {
            null
        }
    }

    override suspend fun generateBackupCipherKey(key: String, input: ByteArray): ByteArray? {
        return try {
            Sdk.generateBackupCipherKey(key, input)
        } catch (exception: Exception) {
            null
        }
    }

    override suspend fun generateMnemonicsFromBackupKey(backupKey: ByteArray): String? {
        return try {
            Sdk.backupMnemonicFromKey(backupKey)
        } catch (exception: Exception) {
            null
        }
    }

    override suspend fun derivePrivateKeyFromMnemonics(mnemonics: String): ByteArray? {
        return try {
            Sdk.backupMnemonicToKey(mnemonics)
        } catch (exception: Exception) {
            null
        }
    }
}
