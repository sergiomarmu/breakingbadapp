import Data.androidxJunitVersion
import Data.coroutinesVersion
import Data.gsonVersion
import Data.hiltVersion
import Data.jUnitVersion
import Data.kotlinCoroutinesTestVersion
import Data.kotlinVersion
import Data.loggingInterceptorVersion
import Data.mockitoCoreVersion
import Data.mockitoWebServerVersion
import Data.retrofit2ConverterGsonVersion
import Data.retrofit2Version

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
    implementation(project(":core"))

    // region kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    // endregion kotlin

    // region gson
    implementation("com.google.code.gson:gson:$gsonVersion")
    // endregion gson

    // region retrofit
    implementation("com.squareup.retrofit2:retrofit:$retrofit2Version")
    implementation("com.squareup.okhttp3:logging-interceptor:$loggingInterceptorVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit2ConverterGsonVersion")
    // endregion retrofit

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
    implementation("org.mockito:mockito-core:$mockitoCoreVersion")
    testImplementation("com.squareup.okhttp3:mockwebserver:$mockitoWebServerVersion")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinCoroutinesTestVersion")
    // endregion test
}