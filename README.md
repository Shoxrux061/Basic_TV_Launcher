📺 Android TV Launcher

A simple custom Home Launcher for Android TV.
Displays all installed launchable applications and allows the user to open them directly using a remote control.
Supports both Android TV and Google TV devices.

🎯 Features

Lists all installed apps that include either LAUNCHER or LEANBACK_LAUNCHER intent filters.

Shows app icon and app name for each entry.

Launches the selected app via startActivity().

Handles HOME button presses correctly when set as the default launcher.

Works automatically after boot when chosen as the default launcher.

(Optional) Persists basic state using SharedPreferences.

⚙️ Technical Details
Property	Value
minSdkVersion	21 (Android 5.0 Lollipop)
targetSdkVersion	35 (Android 14)
Language	Kotlin
Architecture	MVVM
UI	Jetpack Compose for TV
Dependency Injection	Hilt
Asynchronous Tasks	Kotlin Coroutines
🧩 Build & Run
Option 1. Run in Android Studio (TV Emulator)

Open the project in Android Studio.

Select a TV AVD (e.g., Android TV (1080p)).

Build and run the app (Run → Run 'app').

The launcher will appear in the installed apps list.

Option 2. Install the APK

Install the generated app-debug.apk on your Android TV or emulator.

Assign it as the default launcher manually (required for testing):

adb shell cmd package set-home-activity your.package.name/.MainActivity


(replace your.package.name with the actual package name from the manifest)

🚀 Testing the Launcher
Verify App List

When the launcher opens, you’ll see a grid of installed apps.

Each item shows:

App icon

App name

Launch Apps

Select an app and press OK on the remote (or Enter in the emulator).

If the app has a valid launch intent — it opens.

Otherwise, a toast message appears: “Unable to open the app.”

Excluded Apps

Certain system packages like Settings, Play Games, or SystemUI don’t expose launchable activities.
They are automatically filtered and not shown in the app list.

Screenshots


<img width="801" height="463" alt="image" src="https://github.com/user-attachments/assets/f7cf8dd0-7632-41a6-a424-d540783dc045" />
