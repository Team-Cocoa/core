dependencies {
    compileOnly(libs.paper.api)
    compileOnly(files("libs/VulcanAPI.jar"))
    compileOnly(project(":core"))
    compileOnly(project(":cloudnet"))
    api(libs.packetevents.spigot)
}
