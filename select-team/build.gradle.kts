plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = ConfigData.compileSdkVersion

    defaultConfig {
        minSdk = ConfigData.minSdkVersion
        targetSdk = ConfigData.targetSdkVersion
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    dataBinding {
        isEnabled = true
    }
}

dependencies {

    implementation(project(":domain"))
    implementation(project(":common-ui"))
    implementation(project(":core-model"))

    //Firebase
    implementation(Dependencies.firebase.analytics)
    implementation(Dependencies.firebase.cloudMessaging)

    // Hilt
    androidTestImplementation(Dependencies.androidTest.hilt)
    kaptAndroidTest(Dependencies.hilt.compiler)
    implementation(Dependencies.hilt.android)
    kapt(Dependencies.hilt.compiler)

    // Test
    testImplementation(Dependencies.Test.junit)
    androidTestImplementation(Dependencies.androidX.junit)
    androidTestImplementation(Dependencies.AndroidTest.espressoCore)
}