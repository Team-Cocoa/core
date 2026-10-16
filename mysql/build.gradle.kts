dependencies {
    api(libs.mysql.connector)
    compileOnly(project(":core"))

    testImplementation(project(":core"))
}
