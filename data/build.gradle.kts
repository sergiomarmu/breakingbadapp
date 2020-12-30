import Data.androidxJunitVersion
import Data.androidxTestRunnerVersion
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
import Data.roomVersion

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
    // see https://square.github.io/retrofit/
    implementation("com.squareup.retrofit2:retrofit:$retrofit2Version")
    implementation("com.squareup.okhttp3:logging-interceptor:$loggingInterceptorVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit2ConverterGsonVersion")
    // endregion retrofit

    // region coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    // endregion coroutines

    // region hilt
    // see https://dagger.dev/hilt/
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
    // endregion hilt

    // region room
    // see https://developer.android.com/training/data-storage/room
    api("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    // Kotlin Extensions and Coroutines support for Room
    api("androidx.room:room-ktx:$roomVersion")
    // Test helpers
    testImplementation("androidx.room:room-testing:$roomVersion")
    // endregion room

    // region test
    testImplementation("junit:junit:$jUnitVersion")
    androidTestImplementation("junit:junit:$jUnitVersion")
    androidTestImplementation("androidx.test.ext:junit:$androidxJunitVersion")
    androidTestImplementation("androidx.test:runner:$androidxTestRunnerVersion")
    // see https://site.mockito.org/
    implementation("org.mockito:mockito-core:$mockitoCoreVersion")
    // see https://github.com/square/okhttp/tree/master/mockwebserver
    testImplementation("com.squareup.okhttp3:mockwebserver:$mockitoWebServerVersion")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinCoroutinesTestVersion")
    // endregion test
}