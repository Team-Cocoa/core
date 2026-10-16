plugins {
    java
    alias(libs.plugins.shadow) apply false
}

allprojects {
    group = "kr.teamcocoa.system"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
        maven("https://central.sonatype.com/repository/maven-snapshots/")
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.codemc.io/repository/maven-releases/")
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        mavenLocal {
            content {
                includeGroup("org.spigotmc")
            }
        }
    }
}

subprojects {
    apply(plugin = "java-library")

    val libs = rootProject.libs

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(17)
        }
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }

    dependencies {
        compileOnly(libs.lombok)
        annotationProcessor(libs.lombok)
        testCompileOnly(libs.lombok)
        testAnnotationProcessor(libs.lombok)

        compileOnly(libs.cloudnet.driver)
        compileOnly(libs.cloudnet.wrapper)
        compileOnly(libs.cloudnet.platform.inject.api)
        compileOnly(libs.cloudnet.bridge)
        annotationProcessor(libs.cloudnet.platform.inject.processor)
        testImplementation(libs.cloudnet.driver)

        testImplementation(platform(libs.junit.bom))
        testImplementation(libs.junit.jupiter)
        testRuntimeOnly(libs.junit.platform.launcher)
    }

    tasks.test {
        useJUnitPlatform()
    }
}
