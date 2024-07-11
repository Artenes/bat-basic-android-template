import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName
import java.util.Properties

val propertiesFile = rootProject.file("secrets.properties")
val secrets = Properties()

if (propertiesFile.exists()) {
    secrets.load(propertiesFile.inputStream())
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt.gradle)
    alias(libs.plugins.ksp)
}

android {

    if (
        secrets.containsKey("keystorePath")
        && secrets.containsKey("keystorePassword")
        && secrets.containsKey("keystoreAlias")
        && secrets.containsKey("keystoreAliasPassword")
    ) {
        signingConfigs {
            create("release") {
                storeFile = file(secrets["keystorePath"] as String)
                storePassword = secrets["keystorePassword"] as String
                keyAlias = secrets["keystoreAlias"] as String
                keyPassword = secrets["keystoreAliasPassword"] as String
            }
        }
    }

    namespace = "com.android.app.bat"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.android.app.bat"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        archivesName = "template_v${versionName}"
        testInstrumentationRunner = "com.android.app.bat.HiltTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        // Enable room auto-migrations
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }

    buildTypes {
        getByName("debug") {
            //applicationIdSuffix = ".debug" #add suffix to package name if necessary
            versionNameSuffix = "-debug"
        }

        signingConfigs.findByName("release")?.let {
            getByName("release") {
                isMinifyEnabled = true
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
                signingConfig = it
                //applicationIdSuffix = ".release" #add suffix to package name if necessary
                versionNameSuffix = "-release"
            }
        }

    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        aidl = false
        buildConfig = true
        renderScript = false
        shaders = false
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidxComposeCompiler.get()
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Desugar support for new java APIs for old Android versions
    coreLibraryDesugaring(libs.android.tools.desugar)

    // Core Android dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Timber
    implementation(libs.timber)

    // Icons
    implementation(libs.icons.extended)

    // Hilt Dependency Injection
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Hilt and instrumented tests
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.android.compiler)

    // Hilt and Robolectric tests
    testImplementation(libs.hilt.android.testing)
    kaptTest(libs.hilt.android.compiler)

    //DataStore
    implementation(libs.datastore)

    // Arch Components
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Compose
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.accompanist.permission)

    // Tooling
    debugImplementation(libs.androidx.compose.ui.tooling)

    // Instrumented tests
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Local tests: jUnit, coroutines, Android runner
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)

    // Instrumented tests: jUnit rules and runners
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.runner)

}
