dependencies {
    compileOnly(libs.paperspigot.api)
    compileOnly(libs.jetbrains.annotations)
    compileOnly(files("libs/VulcanAPI.jar"))
    compileOnly(project(":core"))
    compileOnly(project(":cloudnet"))
    compileOnly(project(":retrofit"))
    api(libs.packetevents.spigot)
}
