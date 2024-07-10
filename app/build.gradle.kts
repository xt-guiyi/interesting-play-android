plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.serialization)
}

android {
    namespace = "com.example.loveLife"
    compileSdk = 34
    // https://developer.android.com/build/configure-app-module?hl=zh-cn#set-application-id
    defaultConfig {
        applicationId = "com.example.loveLife"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    buildFeatures {
        viewBinding = true
        dataBinding = false
    }

    // https://developer.android.com/build/jdks?hl=zh-cn#source-compat
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.constraintlayout)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation(libs.junit)
    implementation(libs.material)
    implementation(libs.datastore.preferences)
    implementation(libs.datastore.preferences.core)
    implementation(libs.jetbrains.kotlinx.serialization.json)
    implementation(libs.lifecycle.viewmodel.ktx)
    // retrofit网络请求框架
    implementation(libs.retrofit)
    implementation(libs.squareup.converter.gson)
    // 吐司框架：https://github.com/getActivity/Toaster
    implementation(libs.getactivity.toaster)
    // glide 图片加载 https://mvnrepository.com/artifact/com.github.bumptech.glide/glide
    implementation(libs.github.glide)
}