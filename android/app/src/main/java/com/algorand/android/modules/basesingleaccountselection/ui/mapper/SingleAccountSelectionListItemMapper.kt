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

package com.algorand.android.modules.basesingleaccountselection.ui.mapper

import com.algorand.android.models.AccountIconResource
import com.algorand.android.modules.basesingleaccountselection.ui.model.SingleAccountSelectionListItem
import com.algorand.android.utils.AccountDisplayName
import javax.inject.Inject

class SingleAccountSelectionListItemMapper @Inject constructor() {

    fun mapToAccountItem(
        accountDisplayName: AccountDisplayName,
        accountIconResource: AccountIconResource,
        accountFormattedPrimaryValue: String?,
        accountFormattedSecondaryValue: String?
    ): SingleAccountSelectionListItem.AccountItem {
        return SingleAccountSelectionListItem.AccountItem(
            accountDisplayName = accountDisplayName,
            accountIconResource = accountIconResource,
            accountFormattedPrimaryValue = accountFormattedPrimaryValue,
            accountFormattedSecondaryValue = accountFormattedSecondaryValue
        )
    }
}
