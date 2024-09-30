plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.vertex.io"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.vertex.io"
        minSdk = 26
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
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

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation ("com.github.ybq:Android-SpinKit:1.4.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation ("com.basgeekball:awesome-validation:4.3")
    implementation ("io.github.otpless-tech:otpless-android-sdk:2.4.0")
    implementation ("io.github.otpless-tech:otpless-android-sdk:2.4.3")
    implementation ("androidx.core:core:1.13.1")
    implementation ("androidx.core:core-ktx:1.13.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-database:21.0.0")
    implementation("com.google.firebase:firebase-inappmessaging-display:21.0.0")
    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation("com.google.android.gms:play-services-ads:23.3.0")
    implementation ("com.google.android.gms:play-services-ads:21.3.0")
    implementation("com.google.android.gms:play-services-ads-lite:23.3.0")
    implementation ("com.airbnb.android:lottie:3.4.0")
    implementation("androidx.activity:activity-ktx:1.9.2")
    implementation("androidx.activity:activity:1.9.2")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.compose.animation:animation-android:1.7.1")
    implementation("androidx.compose.foundation:foundation-android:1.7.1")
    implementation("androidx.compose.material3:material3-android:1.3.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    implementation("com.onesignal:OneSignal:5.1.13")
    implementation ("com.google.android.recaptcha:recaptcha:18.6.0")
    implementation ("org.imaginativeworld.oopsnointernet:oopsnointernet:2.0.0")
    implementation ("com.github.Drjacky:ImagePicker:2.3.22")
    implementation ("androidx.appcompat:appcompat:1.7.0")
    implementation ("androidx.cardview:cardview:1.0.0")
    implementation ("com.google.firebase:firebase-storage:21.0.0")
    implementation ("com.github.bumptech.glide:glide:4.13.2")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:4.9.3")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation ("org.tensorflow:tensorflow-lite:2.10.0")
    implementation ("org.tensorflow:tensorflow-lite-gpu:2.10.0")
}