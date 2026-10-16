plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "system"

include(
    "core",
    "cloudnet",
    "bukkit",
    "bukkit-legacy",
    "bukkit-modern",
    "bungee",
    "retrofit",
    "jar",
)
