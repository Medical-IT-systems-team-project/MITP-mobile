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

### Doctor Interface
<img src="/screenshots/main_light-portrait.png" alt="Doctor" width="300"/> <img src="/screenshots/main_dark-portrait.png" alt="Doctor" width="300"/> 
<img src="/screenshots/patient_list_light-portrait.png" alt="Doctor2" width="300"/> <img src="/screenshots/patient_list_dark-portrait.png" alt="Doctor" width="300"/> 

### Case Management
<img src="/screenshots/case_info_light-portrait.png" alt="Case" width="300"/> <img src="/screenshots/case_info_dark-portrait.png" alt="Doctor" width="300"/> 
<img src="/screenshots/treatments_light-portrait.png" alt="Case2" width="300"/> <img src="/screenshots/treatments_dark-portrait.png" alt="Case2" width="300"/> 
<img src="/screenshots/medication_light-portrait.png" alt="Case3" width="300"/> <img src="/screenshots/medication_dark-portrait.png" alt="Case2" width="300"/> 
<img src="/screenshots/new_treatment_light-portrait.png" alt="Treatment_New" width="300"/> <img src="/screenshots/new_treatment_dark-portrait.png" alt="Case2" width="300"/> 
<img src="/screenshots/new_medication_light-portrait.png" alt="Medication_New" width="300"/> <img src="/screenshots/new_medication-portrait.png" alt="Case2" width="300"/> 

### Patient Management
<img src="/screenshots/new_patient_light-portrait.png" alt="Patient_New" width="300"/> <img src="/screenshots/new_patient_dark-portrait.png" alt="Case2" width="300"/> 
<img src="/screenshots/share_id_light-portrait.png" alt="Patient_Share" width="300"/> <img src="/screenshots/share_id_dark-portrait.png" alt="Case2" width="300"/> 

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




