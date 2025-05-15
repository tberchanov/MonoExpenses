import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    kotlin("plugin.serialization") version libs.versions.kotlin
    alias(libs.plugins.sqlDelight)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    jvm()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
    }

    applyDefaultHierarchyTemplate()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)
            implementation(libs.ktor.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kermit)
            implementation(libs.koin.core)
        }

        androidMain {
            dependencies {
                implementation(libs.ktor.client.okhttp)
                implementation(libs.sqldelight.android)
            }
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.sqldelight.native)
        }

        jvmMain.dependencies {
            implementation(libs.credential.secure.storage)
            implementation(libs.sqldelight.sqlite)
        }

        nativeMain.dependencies {
            implementation(libs.sqldelight.native)
        }

        wasmJsMain.dependencies {
            // sqldelight is not supporting wasmJs
            configurations["wasmJsMainImplementation"].exclude(
                group = "app.cash.sqldelight",
                module = "runtime"
            )
            configurations["wasmJsMainImplementation"].exclude(
                group = "app.cash.sqldelight",
                module = "async-extensions"
            )
        }

        val sqldelightMain by creating {
            dependsOn(commonMain.get())

            dependencies {

            }
        }
        androidMain.get().dependsOn(sqldelightMain)
        iosMain.get().dependsOn(sqldelightMain)
        jvmMain.get().dependsOn(sqldelightMain)
        nativeMain.get().dependsOn(sqldelightMain)
    }
}

android {
    namespace = "com.monoexpenses.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}

sqldelight {
    databases {
        create("Database") {
            packageName.set("com.monoexpenses")
            generateAsync.set(true)
            srcDirs(listOf("src/sqldelightMain/sqldelight"))
        }
    }
}
