java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

dependencies {
    compileOnly(libs.paper.api)
    compileOnly(project(":core"))
    compileOnly(project(":bukkit"))
}
