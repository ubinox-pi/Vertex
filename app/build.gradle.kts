plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
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
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-database:21.0.0")
    implementation("com.google.firebase:firebase-inappmessaging-display:21.0.0")
    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation("com.google.android.gms:play-services-ads:23.1.0")
    implementation("com.google.android.gms:play-services-ads-lite:23.1.0")
    implementation ("com.airbnb.android:lottie:3.4.0")
    implementation("androidx.activity:activity:1.9.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.onesignal:OneSignal:5.1.13")
    implementation ("com.google.android.recaptcha:recaptcha:18.5.1")
    implementation ("org.imaginativeworld.oopsnointernet:oopsnointernet:2.0.0")
}