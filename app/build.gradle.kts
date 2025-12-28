plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.weatherdemo"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.weatherdemo"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
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

    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.cardview:cardview:1.0.0")


    implementation ("com.google.android.material:material:1.8.0")

    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.10.0")

    implementation ("androidx.activity:activity-ktx:1.7.2")

    implementation("androidx.recyclerview:recyclerview:1.2.1")

    // Firebase SDK
    implementation("com.google.firebase:firebase-auth:21.1.0") // Firebase Authentication untuk login
    implementation("com.google.firebase:firebase-database:20.0.5") // Firebase Realtime Database

    // Firebase BoM (Bill of Materials)
    implementation(platform("com.google.firebase:firebase-bom:30.3.1"))  // Pastikan semua Firebase SDK menggunakan versi yang kompatibel
}
apply(plugin = "com.google.gms.google-services")