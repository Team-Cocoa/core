plugins {
    alias(libs.plugins.shadow)
}

java {
    disableAutoTargetJvm()
}

dependencies {
    implementation(project(":core"))
    implementation(project(":bukkit"))
    implementation(project(":bukkit-legacy"))
    implementation(project(":bukkit-modern"))
    implementation(project(":bungee"))
    implementation(project(":retrofit"))
    implementation(project(":cloudnet"))
}

tasks.shadowJar {
    archiveFileName = "TeamCocoa-Core.jar"
    mergeServiceFiles()
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    filesNotMatching("META-INF/services/**") {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
    relocate("com.github.retrooper.packetevents", "kr.teamcocoa.core.bukkit.packetevents.api")
    relocate("io.github.retrooper.packetevents", "kr.teamcocoa.core.bukkit.packetevents.impl")
    relocate("kotlin", "kr.teamcocoa.core.libs.kotlin")
}

tasks.jar {
    enabled = false
}

tasks.build {
    dependsOn(tasks.shadowJar)
}
