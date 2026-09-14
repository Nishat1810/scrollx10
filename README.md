# Scroll X10

A clean-room Android automatic scrolling app inspired by the supplied floating-controller screenshots.

## Features
- Floating overlay controller: stop, play/pause, settings.
- AccessibilityService performs real swipe gestures.
- Speed from **0.1× through 20×**, with the requested **10×** range included.
- Reverse direction option.
- Settings remain in the main app.
- Drag the floating controller around the screen.

## Build on GitHub
Upload this folder to a GitHub repository. The included GitHub Actions workflow builds `app-debug.apk` automatically on pushes to `main` or from **Actions → Build APK → Run workflow**.

## Android permissions
The user must manually enable:
1. **Accessibility** → Scroll X10 → allow the service.
2. **Display over other apps** → allow the overlay.

The accessibility service is used only to perform scrolling gestures; it does not request screen-content retrieval.

## Note
This is a clean-room implementation of the requested functionality, not the original application's source code. It intentionally uses its own package name and implementation.


## Direction control
The main settings screen includes four directions: **Scroll up**, **Scroll down**, **Scroll left**, and **Scroll right**. The selected direction is used by the floating overlay controller. The Reverse Direction switch reverses whichever direction is selected.
