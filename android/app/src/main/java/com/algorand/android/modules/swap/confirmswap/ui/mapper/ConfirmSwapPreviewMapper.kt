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

package com.algorand.android.modules.swap.confirmswap.ui.mapper

import com.algorand.android.models.AnnotatedString
import com.algorand.android.modules.accounticon.ui.model.AccountIconDrawablePreview
import com.algorand.android.modules.swap.assetswap.domain.model.SwapQuote
import com.algorand.android.modules.swap.confirmswap.ui.mapper.decider.ConfirmSwapPriceImpactWarningStatusDecider
import com.algorand.android.modules.swap.confirmswap.ui.model.ConfirmSwapPreview
import com.algorand.android.modules.swap.utils.priceratioprovider.SwapPriceRatioProvider
import com.algorand.android.utils.AccountDisplayName
import com.algorand.android.utils.ErrorResource
import com.algorand.android.utils.Event
import javax.inject.Inject

class ConfirmSwapPreviewMapper @Inject constructor(
    private val confirmSwapPriceImpactWarningStatusDecider: ConfirmSwapPriceImpactWarningStatusDecider
) {

    @Suppress("LongParameterList")
    fun mapToConfirmSwapPreview(
        fromAssetDetail: ConfirmSwapPreview.SwapAssetDetail,
        toAssetDetail: ConfirmSwapPreview.SwapAssetDetail,
        priceRatioProvider: SwapPriceRatioProvider,
        slippageTolerance: String,
        formattedPriceImpact: String,
        minimumReceived: AnnotatedString,
        swapQuote: SwapQuote,
        isLoading: Boolean,
        priceImpact: Float,
        formattedExchangeFee: String,
        formattedPeraFee: String,
        accountIconDrawablePreview: AccountIconDrawablePreview,
        accountDisplayName: AccountDisplayName,
        errorEvent: Event<ErrorResource>?,
        slippageToleranceUpdateSuccessEvent: Event<Unit>?
    ): ConfirmSwapPreview {
        return ConfirmSwapPreview(
            fromAssetDetail = fromAssetDetail,
            toAssetDetail = toAssetDetail,
            priceRatioProvider = priceRatioProvider,
            slippageTolerance = slippageTolerance,
            formattedPriceImpact = formattedPriceImpact,
            minimumReceived = minimumReceived,
            formattedExchangeFee = formattedExchangeFee,
            formattedPeraFee = formattedPeraFee,
            swapQuote = swapQuote,
            isLoading = isLoading,
            priceImpactWarningStatus = confirmSwapPriceImpactWarningStatusDecider.decideWarningStatus(priceImpact),
            accountIconDrawablePreview = accountIconDrawablePreview,
            accountDisplayName = accountDisplayName,
            errorEvent = errorEvent,
            slippageToleranceUpdateSuccessEvent = slippageToleranceUpdateSuccessEvent
        )
    }
}
