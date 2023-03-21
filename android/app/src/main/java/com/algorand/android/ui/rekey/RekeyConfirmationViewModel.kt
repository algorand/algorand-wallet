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

package com.algorand.android.ui.rekey

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.algorand.android.core.BaseViewModel
import com.algorand.android.models.Account
import com.algorand.android.models.AccountCacheData
import com.algorand.android.models.SignedTransactionDetail
import com.algorand.android.repository.TransactionsRepository
import com.algorand.android.usecase.AccountAdditionUseCase
import com.algorand.android.usecase.AccountDetailUseCase
import com.algorand.android.utils.AccountCacheManager
import com.algorand.android.utils.Event
import com.algorand.android.utils.MIN_FEE
import com.algorand.android.utils.Resource
import com.algorand.android.utils.analytics.CreationType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.max
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@HiltViewModel
class RekeyConfirmationViewModel @Inject constructor(
    private val accountCacheManager: AccountCacheManager,
    private val transactionsRepository: TransactionsRepository,
    private val accountAdditionUseCase: AccountAdditionUseCase,
    private val accountDetailUseCase: AccountDetailUseCase
) : BaseViewModel() {

    private var sendTransactionJob: Job? = null

    val feeLiveData = MutableLiveData<Long>()
    val transactionResourceLiveData = MutableLiveData<Event<Resource<Any>>>()

    init {
        getFee()
    }

    private fun getFee() {
        viewModelScope.launch(Dispatchers.IO) {
            transactionsRepository.getTransactionParams().use(
                onSuccess = { params ->
                    feeLiveData.postValue(max(REKEY_BYTE_ARRAY_SIZE * params.fee, params.minFee ?: MIN_FEE))
                }
            )
        }
    }

    fun sendRekeyTransaction(transactionDetail: SignedTransactionDetail.RekeyOperation) {
        if (sendTransactionJob?.isActive == true) {
            return
        }

        sendTransactionJob = viewModelScope.launch(Dispatchers.IO) {
            transactionsRepository.sendSignedTransaction(transactionDetail.signedTransactionData).use(
                onSuccess = {
                    with(transactionDetail) {
                        val tempAccount: Account

                        val isReverseRekey = isReverseRekey(accountAddress, rekeyAdminAddress)
                        if (isReverseRekey) {
                            tempAccount = createLedgerAccount(
                                accountAddress = accountAddress,
                                ledgerDetail = ledgerDetail,
                                accountName = accountName
                            )
                        } else {
                            tempAccount = createRekeyedAuthAccount(
                                accountDetail = accountDetail,
                                rekeyAdminAddress = rekeyAdminAddress,
                                ledgerDetail = ledgerDetail,
                                accountAddress = accountAddress,
                                rekeyedAccountDetail = rekeyedAccountDetail,
                                accountName = accountName
                            )
                        }

                        transactionResourceLiveData.postValue(Event(Resource.Success(Any())))
                        // TODO: There is a bug which solved in per-3138.
                        //  For the further context, you can take a look at PR description
                        //  https://github.com/Hipo/algorand-android/pull/1897
                        accountAdditionUseCase.addNewAccount(
                            tempAccount = tempAccount,
                            creationType = CreationType.REKEYED
                        )
                    }
                },
                onFailed = { exception, _ ->
                    transactionResourceLiveData.postValue(Event(Resource.Error.Api(exception)))
                }
            )
        }
    }

    fun getAccountCacheData(address: String): AccountCacheData? {
        return accountCacheManager.accountCacheMap.value[address]
    }

    fun getCachedAccountName(address: String): String? {
        return accountCacheManager.accountCacheMap.value[address]?.account?.name
    }

    fun getCachedAccountAuthAddress(address: String): String? {
        return accountDetailUseCase.getAuthAddress(address)
    }

    fun getCachedAccountData(address: String): Account? {
        return accountDetailUseCase.getCachedAccountDetail(address)?.data?.account
    }

    private fun isReverseRekey(accountAddress: String, rekeyAdminAddress: String): Boolean {
        return accountAddress == rekeyAdminAddress
    }

    private fun createLedgerAccount(
        accountAddress: String,
        ledgerDetail: Account.Detail.Ledger,
        accountName: String
    ): Account {
        return Account.create(
            publicKey = accountAddress,
            detail = Account.Detail.Ledger(
                bluetoothAddress = ledgerDetail.bluetoothAddress,
                bluetoothName = ledgerDetail.bluetoothName,
                positionInLedger = ledgerDetail.positionInLedger
            ),
            accountName = accountName
        )
    }

    private fun createRekeyedAuthAccount(
        accountDetail: Account.Detail?,
        rekeyAdminAddress: String,
        ledgerDetail: Account.Detail.Ledger,
        accountAddress: String,
        rekeyedAccountDetail: Account.Detail?,
        accountName: String
    ): Account {
        val newRekeyedAuthDetailMap = mutableMapOf<String, Account.Detail.Ledger>().apply {
            if (accountDetail is Account.Detail.RekeyedAuth) {
                putAll(accountDetail.rekeyedAuthDetail)
            }
            put(rekeyAdminAddress, ledgerDetail)
        }
        val accountSecretKey = accountDetailUseCase.getCachedAccountSecretKey(accountAddress)
        return Account.create(
            publicKey = accountAddress,
            detail = Account.Detail.RekeyedAuth.create(
                authDetail = rekeyedAccountDetail,
                rekeyedAuthDetail = newRekeyedAuthDetailMap,
                secretKey = accountSecretKey
            ),
            accountName = accountName
        )
    }

    companion object {
        private const val REKEY_BYTE_ARRAY_SIZE = 30
    }
}
