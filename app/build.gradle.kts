import App.androidxAppCompatVersion
import App.androidxConstraintLayoutVersion
import App.androidxCoreVersion
import App.androidxEspressoCoreVersion
import App.androidxJunitVersion
import App.androidxNavigationFragmentVersion
import App.androidxNavigationUiVersion
import App.coroutinesVersion
import App.glideCompilerVersion
import App.glideVersion
import App.hiltCompilerVersion
import App.hiltLifecycleViewModelVersion
import App.hiltVersion
import App.jUnitVersion
import App.kotlinVersion
import App.materialDesignVersion
import App.recyclerViewVersion
import App.retrofit2ConverterGsonVersion
import App.retrofit2Version
import App.swipeRefreshLayoutVersion

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdkVersion(29)

    defaultConfig {
        applicationId = "com.sermarmu.breakingbad"
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

    // Hilt uses Java 8 features
    // https://developer.android.com/training/dependency-injection/hilt-android
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    // https://developer.android.com/topic/libraries/view-binding
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":domain"))
    implementation(project(":data"))

    // region kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    // endregion kotlin

    // region androidx
    // see https://developer.android.com/jetpack
    implementation("androidx.core:core-ktx:$androidxCoreVersion")
    implementation("androidx.appcompat:appcompat:$androidxAppCompatVersion")
    implementation("androidx.constraintlayout:constraintlayout:$androidxConstraintLayoutVersion")
    implementation("androidx.navigation:navigation-fragment-ktx:$androidxNavigationFragmentVersion")
    implementation("androidx.navigation:navigation-ui:$androidxNavigationUiVersion")
    implementation("androidx.hilt:hilt-lifecycle-viewmodel:$hiltLifecycleViewModelVersion")
    kapt("androidx.hilt:hilt-compiler:$hiltCompilerVersion")
    implementation("androidx.recyclerview:recyclerview:$recyclerViewVersion")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:$swipeRefreshLayoutVersion")
    // endregion androidx

    // region material design
    // see https://material.io/design
    implementation("com.google.android.material:material:$materialDesignVersion")
    // endregion material design

    // region glide
    // see https://github.com/bumptech/glide
    implementation("com.github.bumptech.glide:glide:$glideVersion")
    kapt("com.github.bumptech.glide:compiler:$glideCompilerVersion")
    // endregion glide

    // region coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    // endregion coroutines

    // region hilt
    // see https://dagger.dev/hilt/
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
    // endregion hilt

    // region retrofit
    // see https://square.github.io/retrofit/
    implementation("com.squareup.retrofit2:retrofit:$retrofit2Version")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit2ConverterGsonVersion")
    // endregion retrofit

    // region test
    testImplementation("junit:junit:$jUnitVersion")
    androidTestImplementation("androidx.test.ext:junit:$androidxJunitVersion")
    androidTestImplementation("androidx.test.espresso:espresso-core:$androidxEspressoCoreVersion")
    // endregion test
}