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
//  BluetoothLoadingView.swift

import UIKit
import Lottie

class BluetoothLoadingView: BaseView {
    
    private let layout = Layout<LayoutConstants>()
    
    override var intrinsicContentSize: CGSize {
        return layout.current.viewSize
    }
    
    private lazy var loadingAnimationView: AnimationView = {
        let loadingAnimationView = AnimationView()
        loadingAnimationView.contentMode = .scaleAspectFit
        let animation = Animation.named("bluetooth_animation")
        loadingAnimationView.animation = animation
        return loadingAnimationView
    }()
    
    // MARK: Setup
    
    override func configureAppearance() {
        backgroundColor = .clear
    }
    
    override func prepareLayout() {
        addSubview(loadingAnimationView)
        
        loadingAnimationView.snp.makeConstraints { make in
            make.edges.equalToSuperview()
        }
    }
}

// MARK: API

extension BluetoothLoadingView {
    func show() {
        loadingAnimationView.play(fromProgress: 0, toProgress: 1, loopMode: .loop)
    }
    
    func stop() {
        loadingAnimationView.stop()
    }
}

extension BluetoothLoadingView {
    private struct LayoutConstants: AdaptiveLayoutConstants {
        let viewSize = CGSize(width: 100.0, height: 24.0)
    }
}