// Copyright 2019 Algorand, Inc.

// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

//    http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

//
//   LedgerPairWarningView.swift

import UIKit

class LedgerPairWarningView: BaseView {

    private let layout = Layout<LayoutConstants>()

    weak var delegate: LedgerPairWarningViewDelegate?

    private lazy var titleLabel: UILabel = {
        UILabel()
            .withFont(UIFont.font(withWeight: .semiBold(size: 16.0)))
            .withTextColor(Colors.Text.primary)
            .withLine(.contained)
            .withAlignment(.center)
            .withText("ledger-pairing-first-warning-title".localized)
    }()

    private lazy var imageView = UIImageView(image: img("img-warning-circle"))

    private lazy var subtitleLabel: UILabel = {
        let firstText = "\("ledger-pairing-first-warning-message-first".localized)\n".attributed([
            .font(UIFont.font(withWeight: .regular(size: 14.0))),
            .textColor(Colors.Text.primary)
        ])
        let secondText = "ledger-pairing-first-warning-message-second".attributed([
            .font(UIFont.font(withWeight: .semiBold(size: 14.0))),
            .textColor(Colors.Text.primary)
        ])
        let fullText = firstText + secondText

        return UILabel()
            .withLine(.contained)
            .withAlignment(.center)
            .withAttributedText(fullText)
    }()

    private lazy var descriptionLabel: UILabel = {
        UILabel()
            .withFont(UIFont.font(withWeight: .regular(size: 14.0)))
            .withTextColor(Colors.Text.primary)
            .withLine(.contained)
            .withAlignment(.left)
            .withText("ledger-pairing-first-warning-message-description".localized)
    }()

    private lazy var actionButton = MainButton(title: "title-continue".localized)

    override func prepareLayout() {
        setupTitleLabelLayout()
        setupImageViewLayout()
        setupSubtitleLabelLayout()
        setupDescriptionLabelLayout()
        setupActionButtonLayout()
    }

    override func setListeners() {
        actionButton.addTarget(self, action: #selector(notifyDelegateToTakeAction), for: .touchUpInside)
    }

    override func configureAppearance() {
        backgroundColor = Colors.Background.secondary
    }
}

extension LedgerPairWarningView {
    private func setupTitleLabelLayout() {
        addSubview(titleLabel)

        titleLabel.snp.makeConstraints { make in
            make.centerX.equalToSuperview()
            make.top.equalToSuperview().inset(layout.current.topInset)
            make.leading.trailing.equalToSuperview().inset(layout.current.horizontalInset)
        }
    }

    private func setupImageViewLayout() {
        addSubview(imageView)

        imageView.snp.makeConstraints { make in
            make.centerX.equalToSuperview()
            make.top.equalTo(titleLabel.snp.bottom).offset(layout.current.verticalInset)
            make.size.equalTo(layout.current.imageSize)
        }
    }

    private func setupSubtitleLabelLayout() {
        addSubview(subtitleLabel)

        subtitleLabel.snp.makeConstraints { make in
            make.centerX.equalToSuperview()
            make.top.equalTo(imageView.snp.bottom).offset(layout.current.subtitleTopInset)
            make.leading.trailing.equalToSuperview().inset(layout.current.horizontalInset)
        }
    }

    private func setupDescriptionLabelLayout() {
        addSubview(descriptionLabel)

        descriptionLabel.snp.makeConstraints { make in
            make.centerX.equalToSuperview()
            make.top.equalTo(subtitleLabel.snp.bottom).offset(layout.current.descriptionTopInset)
            make.leading.trailing.equalToSuperview().inset(layout.current.descriptionHorizontalInset)
        }
    }

    private func setupActionButtonLayout() {
        addSubview(actionButton)

        actionButton.snp.makeConstraints { make in
            make.centerX.equalToSuperview()
            make.bottom.lessThanOrEqualToSuperview().inset(layout.current.topInset + safeAreaBottom)
            make.leading.trailing.equalToSuperview().inset(layout.current.horizontalInset)
            make.top.equalTo(descriptionLabel.snp.bottom).offset(layout.current.verticalInset)
        }
    }
}

extension LedgerPairWarningView {
    @objc
    private func notifyDelegateToTakeAction() {
        delegate?.ledgerPairWarningViewDidTakeAction(self)
    }
}

extension LedgerPairWarningView {
    private struct LayoutConstants: AdaptiveLayoutConstants {
        let verticalInset: CGFloat = 28.0
        let horizontalInset: CGFloat = 20.0
        let topInset: CGFloat = 16.0
        let subtitleTopInset: CGFloat = 20.0
        let descriptionTopInset: CGFloat = 28.0
        let descriptionHorizontalInset: CGFloat = 16.0
        let imageSize = CGSize(width: 80.0, height: 80.0)
    }
}

protocol LedgerPairWarningViewDelegate: AnyObject {
    func ledgerPairWarningViewDidTakeAction(_ ledgerPairWarningView: LedgerPairWarningView)
}