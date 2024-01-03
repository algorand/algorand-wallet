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

package com.algorand.android.mapper

import androidx.annotation.StringRes
import com.algorand.android.models.AssetOperationResult
import com.algorand.android.utils.AssetName
import javax.inject.Inject

class AssetOperationResultMapper @Inject constructor() {

    fun mapToAssetRemovalOperationResult(
        @StringRes resultTitleResId: Int,
        assetName: AssetName,
        assetId: Long
    ): AssetOperationResult {
        return AssetOperationResult.AssetRemovalOperationResult(
            resultTitleResId = resultTitleResId,
            assetName = assetName,
            assetId = assetId
        )
    }

    fun mapToAssetAdditionOperationResult(
        @StringRes resultTitleResId: Int,
        assetName: AssetName,
        assetId: Long
    ): AssetOperationResult {
        return AssetOperationResult.AssetAdditionOperationResult(
            resultTitleResId = resultTitleResId,
            assetName = assetName,
            assetId = assetId
        )
    }
}
