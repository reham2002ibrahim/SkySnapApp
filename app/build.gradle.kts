plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.skysnapproject"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.skysnapproject"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField(
                "String", "API_KEY", "\"${project.findProperty("API_KEY") as String}\""
            )
        }
        release {
            buildConfigField(
                "String", "API_KEY", "\"${project.findProperty("API_KEY") as String}\""
            )
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true // ✅ ضروري لتفعيل BuildConfig
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Retrofit & Networking
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // WorkManager
    implementation("androidx.work:work-runtime-ktx:2.10.0")

    // Gson
    implementation("com.google.code.gson:gson:2.12.1")

    // Glide for Image Loading
    implementation("com.github.bumptech.glide:compose:1.0.0-beta01")

    // Lifecycle & Coroutines
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")

    // Room Database
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")


    // Location
    implementation("com.google.android.gms:play-services-location:21.1.0")


    //Serialization for NavArgs
    val nav_version = "2.8.8"
    implementation("androidx.navigation:navigation-compose:$nav_version")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")


    //lottie file
    implementation("com.airbnb.android:lottie-compose:6.3.0")

    //matrial
    implementation("androidx.compose.material:material-icons-extended:1.6.0")

    /*    //loading Network image
        implementation("io.coil-kt:coil-compose:2.4.0")

        //fonts
        implementation("com.git.Gurupreet:FontAwesomeCompose:1.0.0")

        //fullLayout

        implementation("com.google.accompanist:accompanist-systemuicontroller:0.33.1-alpha")*/


}
