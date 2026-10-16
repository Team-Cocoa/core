plugins {
    alias(libs.plugins.shadow)
}

dependencies {
    implementation(project(":core"))
    implementation(project(":bukkit"))
    implementation(project(":bungee"))
    implementation(project(":retrofit"))
    implementation(project(":cloudnet"))
}

tasks.shadowJar {
    archiveFileName = "TeamCocoa-Core.jar"
    relocate("com.github.retrooper.packetevents", "kr.teamcocoa.core.bukkit.packetevents.api")
    relocate("io.github.retrooper.packetevents", "kr.teamcocoa.core.bukkit.packetevents.impl")
    relocate("com.squareup.retrofit2", "kr.teamcocoa.core.retrofit.impl")
}

tasks.jar {
    enabled = false
}

tasks.build {
    dependsOn(tasks.shadowJar)
}
