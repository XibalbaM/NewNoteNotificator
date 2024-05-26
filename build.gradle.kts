plugins {
    kotlin("jvm") version "1.9.23"
    application
}

group = "fr.xibalba"
version = "1.0-SNAPSHOT"


repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.github.XibalbaM:PronoteKt:0e10371989")
    implementation("com.google.code.gson:gson:2.10.1")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

application {
    mainClass = "MainKt"
}