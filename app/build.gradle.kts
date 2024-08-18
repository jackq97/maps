plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.jask.olamaps"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.jask.olamaps"
        minSdk = 24
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
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")

    // Required for Ola-MapsSdk
    implementation(files("libs/olamaps.aar"))
    implementation ("com.moengage:moe-android-sdk:12.6.01")
    implementation ("org.maplibre.gl:android-sdk:10.2.0")
    implementation ("org.maplibre.gl:android-sdk-directions-models:5.9.0")
    implementation ("org.maplibre.gl:android-sdk-services:5.9.0")
    implementation ("org.maplibre.gl:android-sdk-turf:5.9.0")
    implementation ("org.maplibre.gl:android-plugin-markerview-v9:1.0.0")
    implementation ("org.maplibre.gl:android-plugin-annotation-v9:1.0.0")

    // Used in sample app
    implementation ("androidx.lifecycle:lifecycle-extensions:2.0.0")
    implementation ("androidx.lifecycle:lifecycle-compiler:2.0.0")
    implementation ("androidx.core:core-ktx:1.10.1")
    implementation ("androidx.appcompat:appcompat:1.5.1")
    implementation ("com.google.android.material:material:1.7.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation ("androidx.lifecycle:lifecycle-extensions:2.0.0")
    implementation ("androidx.lifecycle:lifecycle-compiler:2.0.0")

    val lifecycle_version = "2.8.4"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")

    implementation("com.google.android.gms:play-services-location:21.0.1")


}