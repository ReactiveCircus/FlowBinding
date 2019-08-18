#!/usr/bin/env bash
# fail if any commands fails
set -e
# debug log
set -x

API_LEVEL=23
BUILD_TOOLS_VERSION=29.0.2

sdkmanager "system-images;android-${API_LEVEL};google_apis;x86"
sdkmanager "build-tools;${BUILD_TOOLS_VERSION}"
echo "y" | sdkmanager --update

echo no | avdmanager create avd --force --name "api-${API_LEVEL}" --abi "google_apis/x86" --package "system-images;android-${API_LEVEL};google_apis;x86" --device "Nexus 5X"

"$ANDROID_HOME"/emulator/emulator-headless -avd "api-${API_LEVEL}" -gpu swiftshader -no-snapshot -noaudio -no-boot-anim -camera-back none > /dev/null 2>&1 &

adb wait-for-device

"$ANDROID_HOME"/platform-tools/adb shell settings put global window_animation_scale 0.0
"$ANDROID_HOME"/platform-tools/adb shell settings put global transition_animation_scale 0.0
"$ANDROID_HOME"/platform-tools/adb shell settings put global animator_duration_scale 0.0

./gradlew connectedDebugAndroidTest -Dorg.gradle.daemon=false -Dkotlin.incremental=false --no-parallel
