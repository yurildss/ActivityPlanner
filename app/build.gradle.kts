plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.todo"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.todo"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.example.todo.HiltTestRunner"
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
    buildFeatures {
        compose = true
    }


}


dependencies {
    // Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.10.0"))
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation("com.google.firebase:firebase-analytics")

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0") // não tá no catálogo, pode adicionar se quiser

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose.android)
    implementation(libs.androidx.core.ktx)

    // Google Auth
    implementation(libs.play.services.auth)

    // Kotlinx datetime
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")

    // Testes unitários
    testImplementation(libs.junit)
    testImplementation("org.mockito:mockito-core:3.11.2")
    testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.2")
    implementation(kotlin("test"))

    // Testes instrumentados
    androidTestImplementation(libs.androidx.junit) // esse precisa ser 1.1.5 no catálogo, não 1.2.1
    androidTestImplementation(libs.androidx.espresso.core) // precisa ser 3.5.0 no catálogo
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.51.1")
    androidTestImplementation(platform(libs.androidx.compose.bom))  // Add this for test too
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.51.1")

    // Debug/Test tooling
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
