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

package com.algorand.android.usecase

import com.algorand.android.R
import com.algorand.android.core.BaseUseCase
import com.algorand.android.mapper.WarningConfirmationMapper
import com.algorand.android.models.Account
import com.algorand.android.models.WarningConfirmation
import com.algorand.android.modules.accounts.domain.usecase.AccountDisplayNameUseCase
import com.algorand.android.modules.accountstatehelper.domain.usecase.AccountStateHelperUseCase
import com.algorand.android.repository.AccountRepository
import com.algorand.android.utils.AccountDisplayName
import javax.inject.Inject

class AccountOptionsUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val accountDetailUseCase: AccountDetailUseCase,
    private val warningConfirmationMapper: WarningConfirmationMapper,
    private val accountDisplayNameUseCase: AccountDisplayNameUseCase,
    private val accountStateHelperUseCase: AccountStateHelperUseCase
) : BaseUseCase() {

    fun isThereAnyAsset(publicKey: String): Boolean {
        val accountInformation = accountRepository.getCachedAccountDetail(publicKey)?.data?.accountInformation
        return accountInformation?.isThereAnyDifferentAsset() ?: false
    }

    fun isAccountRekeyed(publicKey: String): Boolean {
        return accountDetailUseCase.isAccountRekeyed(publicKey)
    }

    fun getAuthAddress(publicKey: String): String? {
        return accountDetailUseCase.getAuthAddress(publicKey)
    }

    fun canDisplayPassphrases(publicKey: String): Boolean {
        val secretKey = accountDetailUseCase.getCachedAccountDetail(publicKey)?.data?.account?.getSecretKey()
        return secretKey != null && secretKey.isNotEmpty()
    }

    fun getAccountName(publicKey: String): String {
        return accountDetailUseCase.getAccountName(publicKey)
    }

    fun canAccountSignTransaction(accountAddress: String): Boolean {
        return accountStateHelperUseCase.hasAccountAuthority(accountAddress)
    }

    fun isUndoRekeyPossible(accountAddress: String): Boolean {
        return accountDetailUseCase.isAccountRekeyed(accountAddress) && canAccountSignTransaction(accountAddress)
    }

    fun getRemovingAccountWarningConfirmationModel(publicKey: String): WarningConfirmation {
        val descriptionRes = when (accountDetailUseCase.getAccountType(publicKey)) {
            Account.Type.STANDARD, Account.Type.LEDGER, Account.Type.REKEYED, Account.Type.REKEYED_AUTH -> {
                R.string.you_are_about_to_remove_account
            }

            Account.Type.WATCH -> R.string.you_are_about_to_remove_watch_account
            else -> R.string.you_are_about_to_remove_account
        }
        return warningConfirmationMapper.mapToAccountWarningConfirmation(
            drawableRes = R.drawable.ic_trash,
            titleRes = R.string.remove_account,
            descriptionRes = descriptionRes,
            positiveButtonTextRes = R.string.remove,
            negativeButtonTextRes = R.string.keep_it
        )
    }

    fun getAuthAccountDisplayName(accountAddress: String): AccountDisplayName {
        val authAddress = getAuthAddress(accountAddress).orEmpty()
        return accountDisplayNameUseCase.invoke(authAddress)
    }
}
