/*
 * Copyright 2022 Pera Wallet, LDA
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License
 *
 */

package com.algorand.android.modules.accountdetail.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.algorand.android.models.AccountDetailSummary
import com.algorand.android.models.AccountDetailTab
import com.algorand.android.modules.accountdetail.ui.model.AccountDetailPreview
import com.algorand.android.modules.accountdetail.ui.usecase.AccountDetailPreviewUseCase
import com.algorand.android.modules.tracking.accountdetail.AccountDetailFragmentEventTracker
import com.algorand.android.usecase.AccountDeletionUseCase
import com.algorand.android.utils.Event
import com.algorand.android.utils.getOrThrow
import com.algorand.android.utils.launchIO
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AccountDetailViewModel @Inject constructor(
    private val accountDeletionUseCase: AccountDeletionUseCase,
    savedStateHandle: SavedStateHandle,
    private val accountDetailFragmentEventTracker: AccountDetailFragmentEventTracker,
    private val accountDetailPreviewUseCase: AccountDetailPreviewUseCase
) : ViewModel() {

    val accountPublicKey: String = savedStateHandle.getOrThrow(ACCOUNT_PUBLIC_KEY)
    private val accountDetailTab = savedStateHandle.get<AccountDetailTab?>(ACCOUNT_DETAIL_TAB)

    val accountDetailSummaryFlow: StateFlow<AccountDetailSummary?> get() = _accountDetailSummaryFlow
    private val _accountDetailSummaryFlow = MutableStateFlow<AccountDetailSummary?>(null)

    private val _accountDetailTabArgFlow = MutableStateFlow<Event<Int>?>(null)
    val accountDetailTabArgFlow: StateFlow<Event<Int>?> get() = _accountDetailTabArgFlow

    // TODO Combine accountDetailSummaryFlow and accountDetailPreviewFlow
    private val _accountDetailPreviewFlow = MutableStateFlow<AccountDetailPreview?>(null)
    val accountDetailPreviewFlow: StateFlow<AccountDetailPreview?>
        get() = _accountDetailPreviewFlow

    init {
        initAccountDetailSummary()
        initAccountDetailPreview()
        checkAccountDetailTabArg()
    }

    private fun checkAccountDetailTabArg() {
        viewModelScope.launch {
            accountDetailTab?.tabIndex?.run {
                _accountDetailTabArgFlow.emit(Event(this))
            }
        }
    }

    fun removeAccount(publicKey: String) {
        viewModelScope.launch(Dispatchers.IO) {
            accountDeletionUseCase.removeAccount(publicKey)
        }
    }

    private fun initAccountDetailPreview() {
        _accountDetailPreviewFlow.value = accountDetailPreviewUseCase.getInitialPreview()
    }

    fun initAccountDetailSummary() {
        viewModelScope.launchIO {
            accountDetailPreviewUseCase.getAccountSummaryFlow(accountPublicKey).collectLatest { accountDetailSummary ->
                _accountDetailSummaryFlow.emit(accountDetailSummary)
            }
        }
    }

    fun logAccountDetailAssetsTapEventTracker() {
        viewModelScope.launch {
            accountDetailFragmentEventTracker.logAccountDetailAssetsTapEvent()
        }
    }

    fun logAccountDetailCollectiblesTapEventTracker() {
        viewModelScope.launch {
            accountDetailFragmentEventTracker.logAccountDetailCollectiblesTapEvent()
        }
    }

    fun logAccountDetailTransactionHistoryTapEventTracker() {
        viewModelScope.launch {
            accountDetailFragmentEventTracker.logAccountDetailTransactionHistoryTapEvent()
        }
    }

    fun onSwapClick() {
        viewModelScope.launchIO {
            accountDetailFragmentEventTracker.logAccountDetailSwapButtonClickEvent()
            _accountDetailPreviewFlow.update { preview ->
                accountDetailPreviewUseCase.updatePreviewWithSwapNavigation(
                    accountAddress = accountPublicKey,
                    preview = preview
                )
            }
        }
    }

    fun onAddAssetClick() {
        _accountDetailPreviewFlow.update { preview ->
            accountDetailPreviewUseCase.updatePreviewWithAssetAdditionNavigation(
                preview = preview,
                accountAddress = accountPublicKey
            )
        }
    }

    fun onBuySellClick() {
        _accountDetailPreviewFlow.update { preview ->
            accountDetailPreviewUseCase.updatePreviewWithOfframpNavigation(
                preview = preview,
                accountAddress = accountPublicKey
            )
        }
    }

    fun onSendClick() {
        _accountDetailPreviewFlow.update { preview ->
            accountDetailPreviewUseCase.updatePreviewWithSendNavigation(
                preview = preview,
                accountAddress = accountPublicKey
            )
        }
    }

    fun onAssetLongClick(assetId: Long) {
        viewModelScope.launch {
            with(_accountDetailPreviewFlow) {
                emit(accountDetailPreviewUseCase.getAssetLongClickUpdatedPreview(value ?: return@with, assetId))
            }
        }
    }

    companion object {
        private const val ACCOUNT_PUBLIC_KEY = "publicKey"
        private const val ACCOUNT_DETAIL_TAB = "accountDetailTab"
    }
}
