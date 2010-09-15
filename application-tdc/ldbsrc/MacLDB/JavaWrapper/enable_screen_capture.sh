rm -rf ~/Desktop/ScrCapture
defaults write com.apple.screencapture disable-shadow -bool false
defaults write com.apple.screencapture location ~/Desktop
defaults write com.apple.screencapture name picture
killall SystemUIServer

