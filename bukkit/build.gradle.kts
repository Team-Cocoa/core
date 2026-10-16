val minecraftVersion = libs.versions.minecraft.get()

val specialSource = configurations.create("specialSource")
val mojangMappings = configurations.create("mojangMappings")
val spigotMappings = configurations.create("spigotMappings")
val spigotMojangMapped = configurations.create("spigotMojangMapped") { isTransitive = false }
val spigotObfMapped = configurations.create("spigotObfMapped") { isTransitive = false }

dependencies {
    compileOnly(libs.paper.api)
    compileOnly(variantOf(libs.spigot.mojang) { classifier("remapped-mojang") })
    compileOnly(files("libs/VulcanAPI.jar"))
    compileOnly(project(":core"))
    compileOnly(project(":mysql"))
    compileOnly(project(":mongo"))
    compileOnly(project(":cloudnet"))
    api(libs.packetevents.spigot)

    specialSource(libs.specialsource)
    mojangMappings("org.spigotmc:minecraft-server:$minecraftVersion:maps-mojang@txt")
    spigotMappings("org.spigotmc:minecraft-server:$minecraftVersion:maps-spigot@csrg")
    spigotMojangMapped("org.spigotmc:spigot:$minecraftVersion:remapped-mojang")
    spigotObfMapped("org.spigotmc:spigot:$minecraftVersion:remapped-obf")
}

val remapObf = tasks.register<JavaExec>("remapObf") {
    val input = tasks.jar.flatMap { it.archiveFile }
    val output = layout.buildDirectory.file("remap/${project.name}-remapped-obf.jar")
    inputs.file(input)
    inputs.files(mojangMappings, spigotMojangMapped)
    outputs.file(output)
    classpath(specialSource, spigotMojangMapped)
    mainClass = "net.md_5.specialsource.SpecialSource"
    argumentProviders.add(CommandLineArgumentProvider {
        listOf(
            "--live", "--reverse",
            "-i", input.get().asFile.absolutePath,
            "-o", output.get().asFile.absolutePath,
            "-m", mojangMappings.singleFile.absolutePath,
        )
    })
}

val remapSpigot = tasks.register<JavaExec>("remapSpigot") {
    val input = layout.buildDirectory.file("remap/${project.name}-remapped-obf.jar")
    val output = layout.buildDirectory.file("libs/${project.name}-${project.version}-remapped.jar")
    dependsOn(remapObf)
    inputs.file(input)
    inputs.files(spigotMappings, spigotObfMapped)
    outputs.file(output)
    classpath(specialSource, spigotObfMapped)
    mainClass = "net.md_5.specialsource.SpecialSource"
    argumentProviders.add(CommandLineArgumentProvider {
        listOf(
            "--live",
            "-i", input.get().asFile.absolutePath,
            "-o", output.get().asFile.absolutePath,
            "-m", spigotMappings.singleFile.absolutePath,
        )
    })
}

val remapped = configurations.create("remapped") {
    isCanBeConsumed = true
    isCanBeResolved = false
    extendsFrom(configurations.runtimeClasspath.get())
    attributes {
        attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
    }
}

artifacts {
    add(remapped.name, remapSpigot.map { it.outputs.files.singleFile }) {
        builtBy(remapSpigot)
    }
}

tasks.assemble {
    dependsOn(remapSpigot)
}
