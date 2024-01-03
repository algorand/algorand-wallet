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

package com.algorand.android.ui.settings.selection.languageselection

import com.algorand.android.core.BaseViewModel
import com.algorand.android.models.Node
import com.algorand.android.modules.firebase.token.FirebaseTokenManager
import com.algorand.android.utils.analytics.logLanguageChange
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LanguageSelectionViewModel @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics,
    private val firebaseTokenManager: FirebaseTokenManager
) : BaseViewModel() {

    fun logLanguageChange(newLanguageId: String) {
        firebaseAnalytics.logLanguageChange(newLanguageId)
    }

    fun refreshFirebasePushToken(previousNode: Node?) {
        firebaseTokenManager.refreshFirebasePushToken(previousNode)
    }
}
