plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.nutritionproject"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.nutritionproject"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "Alpha 0.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        viewBinding = true
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}
dependencies {
    
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.constraintlayout:constraintlayout-core:1.0.4")
    implementation("com.google.android.gms:play-services-ads-lite:22.3.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("com.android.volley:volley:1.2.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation ("androidx.annotation:annotation:1.3.0")
    implementation ("androidx.core:core-ktx:1.7.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")
    implementation ("androidx.preference:preference-ktx:1.1.1")

    implementation("net.danlew:android.joda:2.12.5")

    testImplementation("junit:junit:4.13.2")

    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.8.0"))

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.code.gson:gson:2.10.1")

    implementation("com.sun.mail:android-mail:1.6.2")
    implementation("com.sun.mail:android-activation:1.6.2")

    implementation("com.google.mlkit:barcode-scanning:17.2.0")
    implementation("com.google.mlkit:object-detection:17.0.0")

    implementation("com.google.mlkit:text-recognition:16.0.0");
    implementation("com.google.android.gms:play-services-mlkit-text-recognition:19.0.0");

    val camerax_version = "1.3.0-rc01"
    implementation("androidx.camera:camera-core:${camerax_version}")
    implementation("androidx.camera:camera-lifecycle:${camerax_version}")
    implementation("androidx.camera:camera-view:${camerax_version}")
    implementation("androidx.camera:camera-extensions:${camerax_version}")

    implementation("com.github.hadibtf:SemiCircleArcProgressBar:1.1.1")

    implementation("com.intuit.sdp:sdp-android:1.1.0")
    implementation("com.intuit.ssp:ssp-android:1.1.0")

    implementation("com.github.blackfizz:eazegraph:1.2.5l@aar")
    implementation("com.nineoldandroids:library:2.4.0")

    implementation("com.michalsvec:single-row-calednar:1.0.0")

    api("com.google.guava:guava:30.1-jre")
}