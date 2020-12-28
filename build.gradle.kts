buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${BreakingBad.gradleVersion}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${BreakingBad.kotlinVersion}")
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