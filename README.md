# Translized Over the Air (OTA) SDK for android

Publish your translations faster and simpler than ever before.
Create release on Translized dashboard and watch as new translations appear in your android app.

## Features
- Over-the-air (OTA) localization
- View inflation

## Requirements
- Android SDK version 21+

## Installation

Add this to your root `build.gradle` at the end of repositories:

   ```groovy
   allprojects {
       repositories {
           ...
           maven { url 'https://jitpack.io' }
       }
   }
   ```

   Add the dependency:

   ```groovy
   dependencies {
       implementation 'com.github.translized:android-SDK:1.0.0'
   }
   ```
   
   If you are using ProGuard, add the following rule:
```
-keep class com.github.translized.** { *; }
```

## Getting started

Before start make sure you have `projectId` and `otaToken`, which you can find in Project/Release and Account/API Access, and make a release on Translized Dashboard/Project/Release.

### Usage
**Configuring the SDK**

In your `Application#onCreate` method initialize the SDK by calling the following lines with your Translized keys:

```kotlin
class App: Application() {
    override fun onCreate() {
        super.onCreate()
        Translized.init(this,"9AXGSpetMe", "123456abc")
        Translized.updateTranslations()
    }
}
```
**Note!** Do not forget to call `Translized.updateTranslations()`

Then in your Activity override `attachBaseContext(..)` to add OTA support for your inflated views.
We recommend you create a base Activity class and extend all your activities from it. 

```kotlin
override fun attachBaseContext(newBase: Context?) {
    super.attachBaseContext(Translized.wrapContext(newBase))
}
```

**Note!** If you don’t have *BaseActivity* class add this code to all of your activities.

## Callbacks

If you would like to be notified when translations are updated use this callback:

```kotlin
Translized.addCallback(object : TranslizedCallback{
    override fun onTranslationsUpdated() {
    }

    override fun onFailure(error: TranslizedError) {
    }

    override fun onUpdateNotNeeded() {
    }
})
```

## View inflation

The SDK will wrap calls to `Resources` and hook into the view inflation to replace any string resources with their latest, cached version.  
Currently there is support for a few attributes of `TextView`, `ImageView` and subclasses thereof.
Menu inflation (option menus, bottom bars, etc.) is currently not supported.

Support for more attributes/types can be added via custom implementations of `Transcriber.Factory` by providing list of transcribers to `Translized#init` call.
 

## Sample

The `./app` directory contains a sample project. To test the OTA features you need to add your own projectId and otaToken.
You can then start replacing the strings found in `res/values/strings.xml` on Translized. New translations will be loaded on app start.

## Support
Contact us through email: **info@translized.com**
