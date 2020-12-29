buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${BreakingBad.gradleVersion}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${BreakingBad.kotlinVersion}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${BreakingBad.hiltVersion}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${BreakingBad.navigationSafeArgsVersion}")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}