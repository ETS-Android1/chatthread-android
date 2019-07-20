# Chat Thread for Android

Chat Thread gives you the base you need to start developing your own Chat application.

[![](https://jitpack.io/v/nathan-fiscaletti/chatthread-android.svg)](https://jitpack.io/#nathan-fiscaletti/chatthread-android)
[![](https://jitpack.io/v/nathan-fiscaletti/chatthread-android/month.svg)](https://jitpack.io/#nathan-fiscaletti/chatthread-android)
[![GitHub license](https://img.shields.io/github/license/nathan-fiscaletti/chatthread-android.svg?color=blue)](https://github.com/nathan-fiscaletti/chatthread-android/blob/master/LICENSE)

<p align="center">
    <img src="screenshots/Preview.png">
</p>

## What's included?

* The Message Thread component.
* Built in support for three core Message types.
    - Text Messages, Image messages, Preview Messages
* The ability to parse a message string into it's respective Message Type.
    - "https://google.com" -> Preview Message
    - "https://mysite.com/some_image.png" -> Image Message
* The ability to implement custom Message Types using your own Views.
* Adapter or List based Message Thread implementations.
* Avatars and images support both Bitmaps and Drawables, load them how you want!
* Much, much more!

## How can I get started?

1. In your **project level** `build.gradle` add the repository

    ```
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
    ```
    
2. In your **module level** `build.gradle` add the library

    ```
    dependencies {
        ...
        implementation 'com.github.nathan-fiscaletti:chatthread-android:v0.1.1'
    }
    ```

Check out the [demo project](./demo) and the [documentation](./docs) for information on how to get started!