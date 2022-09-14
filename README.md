# ZIMKit-Android

## Overview

The following describe how to run the sample code of the In-app Chat UIKit.

## Prepare the environment

Before you begin, make sure your environment meets the following:
- Android Studio 3.6 or later.
- Android SDK Packages: Android SDK 28, Android SDK Platform - Tools 30.x.x or later
- An Android device or Simulator that is running on Android 5.0 or later. We recommend you use a real device. And please enable the "USB Debugging".
- Android device and your computer are connected to the internet.

## Prerequisites

- Go to [ZEGOCLOUD Admin Console\|_blank](https://console.zegocloud.com/) and do the following:
    1.  Create a project, and get the `AppID` and `ServerSecret` of your project.
    2.  Subscribe the **In-app Chat** service.

## Sample code directory structure

```
├── README.md                // README file
├── ZIMKit
│   ├── ZIMKitCommon         // Common libraries
│   ├── ZIMKitConversation   // Session component
│   ├── ZIMKitFull           // Dependencies for all components
│   ├── ZIMKitGroup          // Group component
│   ├── ZIMKitMessages       // Message component
│   ├── app                  // demo module
│   ├── build.gradle         // Global configuration
│   ├── gradle
│   ├── gradle.properties
│   ├── gradlew
│   ├── gradlew.bat
│   ├── local.properties
│   └── settings.gradle      // Module configuration
└── key.jks                  // The signature used to compile release
```

## Run the sample code

1. Replace the `AppID` and `ServerSecret` field in the AppConfig with the AppID and ServerSecret you get.
2. Click the "Sync" button to synchronize the Gradle and all required dependencies.
3. Connect your computer with the Android device, click "Run" button to run the sample code.

## Experience the In-app Chat UIKit

1. On the login page, enter the userID, click "Log in". You will enter the session list page after logged in successfully.
2. You need to create a chat (1-on-1 chat, group chat, or join a chat) first if you are logged in for the first time.
3. After creating a chat, you can send messages on the message page.