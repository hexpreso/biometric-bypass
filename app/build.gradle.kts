import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

plugins {
    alias(libs.plugins.agp.app)
    alias(libs.plugins.kotlin)
}

android {
    namespace = "es.rafagale.biometricbypass"
    compileSdk = 35
    buildToolsVersion = "35.0.0"

    defaultConfig {
        applicationId = "es.rafagale.biometricbypass"
        minSdk = 33
        targetSdk = 35

        versionCode = project.findProperty("versionCode")?.toString()?.toInt() ?: generateVersionCode()
        versionName = project.findProperty("versionName")?.toString() ?: generateVersionName()

        multiDexEnabled = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs["debug"]
        }
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    kotlin {
        jvmToolchain(17)
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    packaging {
        resources {
            merges += "META-INF/xposed/*"
            excludes += "**"
        }
    }

    lint {
        abortOnError = true
        checkReleaseBuilds = false
        disable.add("OldTargetApi")
    }
}

dependencies {
    implementation(libs.libxposed.service)
    compileOnly(libs.libxposed.api)
    implementation(libs.kotlinx.coroutines)
}


fun generateVersionCode(): Int {
    val now = LocalDateTime.now(ZoneOffset.UTC)
    return now.format(DateTimeFormatter.ofPattern("yyMMdd")).toInt()
}

fun generateVersionName(): String {
    val now = LocalDateTime.now(ZoneOffset.UTC)
    return now.format(DateTimeFormatter.ofPattern("dd.MM.yy"))
}
