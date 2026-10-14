# AggregateSieveAnalysis

Aggregate Sieve Analysis is a free, offline Android app for basic aggregate sieve calculations. Enter the weights retained on each sieve to calculate and review an analysis. Saved analyses live in the app's private SQLite database on the device.

## Android compatibility

The current source builds with Android Gradle Plugin 8.13.1, Gradle 8.14, JDK 17, and Android SDK API 36. It targets API 36 for current Google Play requirements and retains the original minimum API 13 (Android 3.2). Android devices also need to support Google Play and the app's screen layout; no app can be guaranteed to run on every Android device. The app has no network permission, ads, analytics, or third-party runtime SDKs.

The original application ID was `io.github.rhemon.aggregatesieveanalysis`. An in-place update would retain existing local records **only if** it were signed with the same private key as the 2018 Play release. That signing key is no longer available, and the old release was not enrolled in Play App Signing. The relaunch therefore uses the new application ID `io.github.rhemon.aggregatesieveanalysis.relaunch` and a matching new content-provider authority. It installs beside the old app and will not automatically receive or migrate its records. The SQLite database name (`asa.db`) and schema version (`1`) are unchanged for future relaunch updates. Keep the old app installed wherever its records are needed; uninstalling it can remove those records. Do not install the debug APK over a Play-installed copy.

## Build

Install JDK 17 and the Android SDK with API 36, then run:

```powershell
.\gradlew.bat :app:assembleDebug :app:bundleRelease :app:lint
```

The debug APK is for testing. To sign a release bundle, set `ASA_UPLOAD_KEYSTORE` to the absolute path of the upload keystore and `ASA_UPLOAD_PASSWORD` to its password before running `:app:bundleRelease`. The upload key alias is `asa2-upload`. The keystore and password are held locally under the ignored `.tools/signing/` directory; keep a separate private backup of both. Never commit a private key, password, or `local.properties`. See [PRIVACY.md](PRIVACY.md) for the public app privacy policy.

Simple android application that will do the very basic sieve analysis of aggregate (finds fineness modulus and uniform coefficient) for you as you provide the weight retained at particular sieves.

Personally I do not have enough knowledge on sieve analysis, so it probably lacks lots of thing that should have been part of sieve analysis, therefore this application is open source,welcoming developers to edit and bring a better and complete app for android.

Here are few TODOs that I have kept for future:<br/>
1. Adding an option to export all data as pdf file, and also allowing to print it.<br/>
2. Giving an option to add other sieve no. on data entry.<br/>
3. Making the graph zoomable <br/>
4. Making the view in TableFragment better<br/>
5. Making the unsaved change warning come only when there is change<br/>
6. Getting a better picture for app icon<br/>

There is a lot more things that can be done. Request to any one interesting on contributing to try completing some these TODOs. 

Thanks 
