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

package com.algorand.android.modules.accounts.domain.usecase

import com.algorand.android.mapper.AccountSummaryMapper
import com.algorand.android.models.AccountDetailSummary
import com.algorand.android.usecase.AccountDetailUseCase
import com.algorand.android.usecase.GetLocalAccountsUseCase
import javax.inject.Inject

class AccountDetailSummaryUseCase @Inject constructor(
    private val getLocalAccountsUseCase: GetLocalAccountsUseCase,
    private val accountSummaryMapper: AccountSummaryMapper,
    private val accountDetailUseCase: AccountDetailUseCase,
    private val getAccountDisplayNameUseCase: AccountDisplayNameUseCase
) {

    fun getAccountDetailSummary(accountAddress: String): AccountDetailSummary {
        val accountDetail = getLocalAccountsUseCase.getLocalAccountsFromAccountManagerCache().firstOrNull {
            it.address == accountAddress
        }
        return accountSummaryMapper.mapToAccountDetailSummary(
            canSignTransaction = accountDetailUseCase.canAccountSignTransaction(accountAddress),
            accountDisplayName = getAccountDisplayNameUseCase.invoke(accountAddress),
            accountAddress = accountAddress,
            accountType = accountDetail?.type
        )
    }
}
