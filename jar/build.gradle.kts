plugins {
    alias(libs.plugins.shadow)
}

dependencies {
    implementation(project(":core"))
    implementation(project(":mysql"))
    implementation(project(":mongo"))
    implementation(project(":bukkit", "remapped"))
    implementation(project(":bungee"))
    implementation(project(":retrofit"))
    implementation(project(":cloudnet"))
}

tasks.shadowJar {
    archiveFileName = "TeamCocoa-Core.jar"
    relocate("com.github.retrooper.packetevents", "kr.teamcocoa.core.bukkit.packetevents.api")
    relocate("io.github.retrooper.packetevents", "kr.teamcocoa.core.bukkit.packetevents.impl")
    relocate("com.mysql", "kr.teamcocoa.core.mysql.jdbc")
    relocate("org.mongodb", "kr.teamcocoa.core.mongo.jdbc")
    relocate("com.squareup.retrofit2", "kr.teamcocoa.core.retrofit.impl")
}

tasks.build {
    dependsOn(tasks.shadowJar)
}
