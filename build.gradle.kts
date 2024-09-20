// Top-level build file where you can add configuration options common to all sub-projects/modules.
/**
 * Use `apply false` in the top-level build.gradle file to add a Gradle
 * plugin as a build dependency but not apply it to the current (root)
 * project. Don't use `apply false` in sub-projects. For more information,
 * see Applying external plugins with same version to subprojects.
 */
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.jetbrains.serialization) apply false
    alias(libs.plugins.compose.compiler) apply false
}