import Core.androidxAppCompatVersion
import Core.androidxJunitVersion
import Core.androidxNavigationFragmentVersion
import Core.coroutinesVersion
import Core.hiltVersion
import Core.jUnitVersion
import Core.kotlinVersion

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdkVersion(29)

    defaultConfig {
        minSdkVersion(27)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    // region kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    // endregion kotlin

    // region androidx
    implementation("androidx.appcompat:appcompat:$androidxAppCompatVersion")
    implementation("androidx.navigation:navigation-fragment-ktx:$androidxNavigationFragmentVersion")
    // endregion androidx

    // region coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    // endregion coroutines

    // region hilt
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
    // endregion hilt

    // region test
    testImplementation("junit:junit:$jUnitVersion")
    androidTestImplementation("androidx.test.ext:junit:$androidxJunitVersion")
    // endregion test
}