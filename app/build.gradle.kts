plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    kotlin("kapt")
}

android {
    namespace = "com.example.mystore"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.mystore"
        minSdk = 26
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
    //configuracion de firma de APK (keystore local)
    signingConfigs{
        create("release"){
            storeFile = file("../keystore/mystore-release.jks")
            storePassword = "mystore1234"
            keyAlias = "key0"
            keyPassword = "mystore1234"
        }
    }

    buildTypes{
        release {
            isMinifyEnabled = false

            //firma del buil release usando el keystore de arriba
            signingConfig = signingConfigs.getByName("release")

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
    packagingOptions {
        resources {
            excludes += listOf(
                "META-INF/LICENSE.md",
                "META-INF/LICENSE-notice.md",
                "META-INF/DEPENDENCIES"
            )
        }
    }
}

dependencies {

    // Retrofit (Para conectar a internet)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // GSON Converter (Para traducir JSON a Kotlin)
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // Coil (Para cargar imágenes de internet)
    implementation("io.coil-kt:coil-compose:2.4.0")

    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.compose.material:material-icons-extended:1.7.4")
    implementation("androidx.compose.material3:material3:1.3.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")

    // Room (base de datos)
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")


    implementation("androidx.compose.ui:ui-tooling-preview")

    // Coil para Jetpack Compose
    implementation("io.coil-kt:coil-compose:2.2.2")

    // Dependencia de ubicacion
    implementation("com.google.android.gms:play-services-location:21.0.1")

    // Kotest
    testImplementation("io.kotest:kotest-runner-junit5:5.8.0")
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")

    // JUnit5
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")

    // Mockk
    testImplementation("io.mockk:mockk:1.13.10")

    androidTestImplementation ("io.mockk:mockk-android:1.13.7")


    androidTestImplementation("androidx.compose.ui:ui-test:1.7.4")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}

//agregamos para usar Junit5
tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}