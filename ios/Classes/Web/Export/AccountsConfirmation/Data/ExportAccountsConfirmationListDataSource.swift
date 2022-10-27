// Copyright 2022 Pera Wallet, LDA

// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

//    http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

//   ExportAccountsConfirmationListDataSource.swift

import Foundation
import MacaroonUIKit
import UIKit

final class ExportAccountsConfirmationListDataSource:
    UICollectionViewDiffableDataSource<ExportAccountsConfirmationListSectionIdentifier, ExportAccountsConfirmationListItemIdentifier> {
    typealias Snapshot =  NSDiffableDataSourceSnapshot<ExportAccountsConfirmationListSectionIdentifier, ExportAccountsConfirmationListItemIdentifier>

    init(
        _ collectionView: UICollectionView,
        hasSingularAccount: Bool
    ) {
        super.init(collectionView: collectionView) {
            collectionView, indexPath, itemIdentifier in

            switch itemIdentifier {
            case .account(let item):
                switch item {
                case .cell(let cellItem):
                    let cell = collectionView.dequeue(
                        ExportAccountsConfirmationListAccountCell.self,
                        at: indexPath
                    )
                    cell.bindData(cellItem.viewModel)
                    return cell
                }
            }
        }

        supplementaryViewProvider = {
            [weak self] collectionView, kind, indexPath in
            guard let section = self?.snapshot().sectionIdentifiers[safe: indexPath.section],
                  section == .accounts,
                  kind == UICollectionView.elementKindSectionHeader else {
                return nil
            }

            let header = collectionView.dequeueHeader(
                ExportAccountsConfirmationListItemHeader.self,
                at: indexPath
            )

            header.bindData(ExportAccountsConfirmationListItemHeaderViewModel(hasSingularAccount: hasSingularAccount))

            return header
        }

        [
            ExportAccountsConfirmationListAccountCell.self,
        ].forEach {
            collectionView.register($0)
        }

        collectionView.register(header: ExportAccountsConfirmationListItemHeader.self)
    }
}
