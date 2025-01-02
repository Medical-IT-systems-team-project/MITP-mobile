# CareTrack - Mobile Medical Case Tracking System

## Summary
CareTrack is a cross-platform mobile application designed for medical case management and tracking. It facilitates communication between doctors and patients through a secure, user-friendly interface. The app allows doctors to manage patient cases, track treatments, and monitor medications, while patients can view their medical history and share their unique identifiers via QR codes.

## Tech Stack

### Core Technologies
- Kotlin Multiplatform 
- Compose Multiplatform
- Compose Cupertino 

### Architecture & DI
- MVVM Architecture
- Koin for Dependency Injection

### Navigation
- Compose Navigation

### Data Management
- DataStore Preferences
- Ktor Client for API communication

### UI Components
- Material Design 3
- Adaptive Composables from Compose Cupertino
- Custom QR Code Generation
- Custom Composables

### Development Tools
- Gradle
- Android Studio

## Features
- Doctor & Patient separate interfaces
- QR Code sharing system
- Case management system
- Treatment tracking
- Medication monitoring
- Dark/Light theme support
- Cross-platform compatibility (iOS & Android)
- UI adaptive to each platform

## Screenshots

### Login
[![Login](/screenshots/login.png)

### Doctor Interface
![Doctor](/screenshots/doctor.png)
![Doctor2](/screenshots/doctor_2.png)

### Patient Interface
![Patient](/screenshots/patient.png)

### Case Management
![Case](/screenshots/case.png)

## Getting Started

### Before running!
- check your system with [KDoctor](https://github.com/Kotlin/kdoctor)
- install JDK 17 or higher on your machine
- add `local.properties` file to the project root and set a path to Android SDK there

### Android
To run the application on android device/emulator:
- open project in Android Studio and run imported android run configuration

To build the application bundle:
- run `./gradlew :composeApp:assembleDebug`
- find `.apk` file in `composeApp/build/outputs/apk/debug/composeApp-debug.apk`
  Run android UI tests on the connected device: `./gradlew :composeApp:connectedDebugAndroidTest`

### iOS
To run the application on iPhone device/simulator:
- Open `iosApp/iosApp.xcproject` in Xcode and run standard configuration
- Or use [Kotlin Multiplatform Mobile plugin](https://plugins.jetbrains.com/plugin/14936-kotlin-multiplatform-mobile) for Android Studio
  Run iOS simulator UI tests: `./gradlew :composeApp:iosSimulatorArm64Test`




