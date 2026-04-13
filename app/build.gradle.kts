plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.moodcalendar"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.moodcalendar"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // 1. 引入 Compose BOM 来管理 Compose 库的版本
    implementation(platform("androidx.compose:compose-bom:2024.02.01"))

    // 2. 添加 Compose 库依赖，**不指定版本号**，由 BOM 统一管理
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    
    // Material Design 依赖（由 BOM 管理）
    // Material3: 用于Compose UI组件（MaterialTheme, Surface, Card等）
    implementation("androidx.compose.material3:material3")
    // Material: 基础组件和图标
    implementation("androidx.compose.material:material")
    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.compose.material:material-icons-extended")

    // 3. 非 Compose BOM 管理的库，保留其版本号
    implementation("androidx.activity:activity-compose:1.8.2") // 保留版本号 OK

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.6") // 保留版本号 OK

    // Hilt for Dependency Injection
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0") // 保留版本号 OK

    // Room Database
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1") // 保留版本号 OK

    // DataStore Preferences
    implementation("androidx.datastore:datastore-preferences:1.0.0") // 保留版本号 OK

    // Kotlin Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2") // 保留版本号 OK

    // Charts Library (Vico) - 这些库不在 Compose BOM 管理范围内
    implementation("com.patrykandpatrick.vico:compose-m3:1.12.0") // 保留版本号 OK
    implementation("com.patrykandpatrick.vico:compose:1.12.0") // 保留版本号 OK

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3") // 保留版本号 OK

    // Lottie for animations
    implementation("com.airbnb.android:lottie-compose:6.3.0") // 保留版本号 OK

    // Coil for image loading
    implementation("io.coil-kt:coil-compose:2.5.0") // 保留版本号 OK

    // Test dependencies
    testImplementation("junit:junit:4.13.2") // 保留版本号 OK
    androidTestImplementation("androidx.test.ext:junit:1.1.5") // 保留版本号 OK
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1") // 保留版本号 OK

    // Android Test - 使用 BOM 管理测试相关的 Compose 库
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.02.01")) // 与主 BOM 版本一致
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}