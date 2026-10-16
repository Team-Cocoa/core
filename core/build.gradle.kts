dependencies {
    compileOnly(libs.gson)
    compileOnly(libs.guava)
    compileOnly(project(":retrofit"))

    testImplementation(libs.gson)
    testImplementation(libs.guava)
    testImplementation(project(":retrofit"))
}
