plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.android")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.vertex.io"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.vertex.io"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        externalNativeBuild {
            cmake {
                cppFlags += ""
            }
        }
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
    buildToolsVersion = "34.0.0"
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    // Android libraries
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("androidx.activity:activity-ktx:1.9.3")
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.cardview:cardview:1.0.0")

    // Firebase platform BOM
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-storage")
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-database:21.0.0")
    implementation("com.google.firebase:firebase-inappmessaging-display:21.0.1")

    // Play Services Ads
    implementation("com.google.android.gms:play-services-ads:23.5.0")
    implementation("com.google.android.gms:play-services-ads-lite:23.5.0")

    // Additional libraries
    implementation("com.github.ybq:Android-SpinKit:1.4.0")
    implementation("com.basgeekball:awesome-validation:4.3")
    implementation("io.github.otpless-tech:otpless-android-sdk:2.4.3")
    implementation("com.airbnb.android:lottie:3.4.0")
    implementation("com.onesignal:OneSignal:5.1.13")
    implementation("com.google.android.recaptcha:recaptcha:18.6.1")
    implementation("org.imaginativeworld.oopsnointernet:oopsnointernet:2.0.0")
    implementation("com.github.Drjacky:ImagePicker:2.3.22")

    // Networking and JSON parsing
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    // Image loading
    implementation("com.github.bumptech.glide:glide:4.13.2")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.activity:activity-ktx:1.9.3")
    implementation("androidx.activity:activity:1.9.3")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
    implementation ("io.coil-kt:coil:2.4.0")


    // Testing libraries
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}
