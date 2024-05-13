plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.dokka)
}

android {
    namespace = "cz.cvut.fit.poberboh.loc_tracker"
    compileSdk = 34

    defaultConfig {
        applicationId = "cz.cvut.fit.poberboh.loc_tracker"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        // Release build configuration
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))

            // Define BASE_URL build configuration field for release build
            buildConfigField("String", "BASE_URL", "\"http://178.128.202.70:8080\"")
        }
        // Debug build configuration
        debug {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))

            // Define BASE_URL build configuration field for debug build
            buildConfigField("String", "BASE_URL", "\"http://178.128.202.70:8080\"")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
    
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}

subprojects {
    apply(plugin = "org.jetbrains.dokka")
}

dependencies {
    implementation(libs.material)

    implementation(libs.osmdroid.android)

    implementation(libs.squareup.okhttp3)
    implementation(libs.squareup.retrofit)
    implementation(libs.squareup.retrofit.converter.gson)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.preference)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.coroutines.android)
    implementation(libs.androidx.navigation.fragment.ktx)

    testImplementation(libs.junit)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.androidx.coroutines.test)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}