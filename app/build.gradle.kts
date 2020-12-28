import App.androidxAppCompatVersion
import App.androidxConstraintLayoutVersion
import App.androidxCoreVersion
import App.androidxEspressoCoreVersion
import App.androidxJunitVersion
import App.hiltVersion
import App.jUnitVersion
import App.kotlinVersion

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdkVersion(29)

    defaultConfig {
        applicationId = "com.sermarmu.breakingbad"
        minSdkVersion(27)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"

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

    // Hilt uses Java 8 features
    // https://developer.android.com/training/dependency-injection/hilt-android
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":domain"))

    // region kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    // endregion kotlin

    // region androidx
    implementation("androidx.core:core-ktx:$androidxCoreVersion")
    implementation("androidx.appcompat:appcompat:$androidxAppCompatVersion")
    implementation("androidx.constraintlayout:constraintlayout:$androidxConstraintLayoutVersion")
    // endregion androidx

    // region hilt
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
    // endregion hilt

    // region test
    testImplementation("junit:junit:$jUnitVersion")
    androidTestImplementation("androidx.test.ext:junit:$androidxJunitVersion")
    androidTestImplementation("androidx.test.espresso:espresso-core:$androidxEspressoCoreVersion")
    // endregion test
}