import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
}

android {
    namespace = "fr.husta.android.dockersearch"
    compileSdk = 36

    defaultConfig {
        applicationId = "fr.husta.android.dockersearch"
        minSdk = 26 // O 8.0
        targetSdk = 34 // Android 14
        versionCode = 134
        versionName = "1.22.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    compileOptions {
        // Flag to enable support for the new language APIs
        // See : https://developer.android.com/studio/write/java8-support#library-desugaring
        isCoreLibraryDesugaringEnabled = true
        // See : https://developer.android.com/studio/write/java8-support.html
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

base {
    archivesName = "android-docker-search-${android.defaultConfig.versionName}"
}

// Create a variable called keystorePropertiesFile, and initialize it to your
// keystore.properties file, in the rootProject folder.
// See: https://developer.android.com/studio/publish/app-signing#secure-shared-keystore
val keystorePropertiesFile = rootProject.file("keystore.properties")

if (keystorePropertiesFile.exists()) {
    // Initialize a new Properties() object called keystoreProperties.
    val keystoreProperties = Properties()

    // Load your keystore.properties file into the keystoreProperties object.
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))

    android {
        signingConfigs {
            create("release") {
                keyAlias = keystoreProperties.getProperty("keyAlias")
                keyPassword = keystoreProperties.getProperty("keyPassword")
                storeFile = file(keystoreProperties.getProperty("storeFile"))
                storePassword = keystoreProperties.getProperty("storePassword")
            }
        }
        buildTypes {
            release {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }
}

dependencies {
    implementation("androidx.core:core:1.17.0")
    implementation("androidx.activity:activity:1.11.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.browser:browser:1.9.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("com.google.android.material:material:1.13.0")

    implementation("org.apache.commons:commons-lang3:3.19.0")
    implementation("io.reactivex.rxjava3:rxjava:3.1.11")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.2")
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-jackson:3.0.0")
    implementation("com.squareup.retrofit2:adapter-rxjava3:3.0.0")
    implementation("com.squareup.okhttp3:okhttp:5.1.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.20.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.20.0")

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.5")

    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.truth:truth:1.4.5")

    // https://developer.android.com/training/testing/set-up-project#android-test-dependencies
    // Core library
    androidTestImplementation("androidx.test:core:1.7.0")
    // AndroidJUnitRunner and JUnit Rules
    androidTestImplementation("androidx.test:runner:1.7.0")
    androidTestImplementation("androidx.test:rules:1.7.0")
    // Assertions
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    // Espresso dependencies
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
}
