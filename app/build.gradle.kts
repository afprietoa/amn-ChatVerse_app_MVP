plugins {
    id("com.android.application")
    id("com.google.gms.google-services") version "4.4.1" apply true
}

android {
    namespace = "aplicacionesmoviles.avanzado.todosalau.ejemplochat"
    compileSdk = 34

    defaultConfig {
        applicationId = "aplicacionesmoviles.avanzado.todosalau.ejemplochat"
        minSdk = 33

        //noinspection EditedTargetSdkVersion
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

    buildFeatures{
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))

    // Firebase Core and Analytics
    implementation("com.google.firebase:firebase-core:21.1.1")
    implementation ("com.google.firebase:firebase-messaging:23.3.1")
    // Firebase Authentication and Firestore
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-storage")
    implementation ("com.google.android.gms:play-services-auth:21.2.0")
    implementation ("com.android.volley:volley:1.2.1")

    implementation ("de.hdodenhof:circleimageview:3.1.0")
    // Add PhotoView dependency
    implementation ("com.github.chrisbanes:PhotoView:2.3.0")

    implementation ("com.github.chrisbanes:PhotoView:2.3.0")

    testImplementation("junit:junit:4.13.2")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")


    // barnav fab//
    implementation ("androidx.coordinatorlayout:coordinatorlayout:1.2.0")


    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    // Room components
    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    // airbnb

    implementation ("com.airbnb.android:lottie:6.4.1")



}