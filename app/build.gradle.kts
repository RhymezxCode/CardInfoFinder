plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "io.rhymezxcode.cardinfofinder"
    compileSdk = 34
    ndkVersion = "26.1.10909125"
    defaultConfig {
        applicationId = "io.rhymezxcode.cardinfofinder"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        vectorDrawables {
            useSupportLibrary = true
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
        multiDexEnabled = true

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    kotlin {
        jvmToolchain {
            languageVersion.set(JavaLanguageVersion.of("17"))
        }
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    hilt {
        enableAggregatingTask = true
        enableExperimentalClasspathAggregation = true
    }
}

dependencies {
    //Base dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.activity.ktx)

    // AndroidX Test Core (for InstantTaskExecutorRule)
    testImplementation(libs.core.testing)

    // Mockito
    testImplementation(libs.mockito.core)

    // MockK
    testImplementation(libs.mockk)

    //Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    // Kotlin Coroutines Test
    testImplementation(libs.kotlinx.coroutines.test)

    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    //Okhttp
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    //Gson
    implementation(libs.gson)

    //Network state observer to check Network state in Real-time
    implementation(libs.network.state.observer)

    //Livedata
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    //Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    // For instrumentation tests
    androidTestImplementation(libs.google.hilt.android.testing)
    kspAndroidTest(libs.hilt.compiler)
    // For local unit tests
    testImplementation(libs.google.hilt.android.testing)
    kspTest(libs.hilt.compiler)


    //brain tree payments Card form
    implementation(libs.card.form)

    //Len24 for OCR
    implementation(libs.lens24)

    //SplashScreen
    implementation(libs.androidx.core.splashscreen)
}