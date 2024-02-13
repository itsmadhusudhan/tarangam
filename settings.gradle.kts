pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

//    buildscript {
//        repositories {
//            google()
//            mavenCentral()
//            gradlePluginPortal()
//        }
//        dependencies {
//            classpath("com.google.dagger:hilt-android-gradle-plugin:2.50")
//        }
//    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Tarangam"
include(":app")
 