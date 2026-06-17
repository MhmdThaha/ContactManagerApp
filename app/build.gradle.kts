plugins {
//    alias(libs.plugins.android.application)
//    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.android.application)
    //alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    // 2. ADD HILT AND ROOM PLUGINS
    id("com.google.devtools.ksp")             // Use KSP instead of kapt for better performance
    id("com.google.dagger.hilt.android")
    id("androidx.room")

    // 2. ADD HILT AND ROOM PLUGINS
  //  id("com.google.devtools.ksp")             // Use KSP instead of kapt for better performance
    id("com.google.dagger.hilt.android")
    id("androidx.room")

}

android {
    namespace = "com.example.contactappfb"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.contactappfb"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    // Icons
    implementation("androidx.compose.material:material-icons-extended")
//implementation(libs.androidx.compose.ui.text)


    // Room (Switched to KSP)
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
   // ksp("androidx.room:room-compiler:$room_version")

    // Hilt (Switched to KSP)
    val hilt_version = "2.51.1"
    implementation("com.google.dagger:hilt-android:$hilt_version")
    //ksp("com.google.dagger:hilt-compiler:$hilt_version")
    // implementation(libs.androidx.hilt.navigation.compose)
// Replace the broken libs. reference with this:
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    // Navigation
    val nav_version = "2.7.7"
    implementation("androidx.navigation:navigation-compose:$nav_version")

    // Coil
    implementation("io.coil-kt:coil-compose:2.7.0")
    //implementation("io.coil-kt:coil-compose:2.4.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}