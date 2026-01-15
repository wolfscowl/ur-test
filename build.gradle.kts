plugins {
    kotlin("jvm") version "2.1.0"
    id("org.jetbrains.dokka") version "2.1.0"
    id("maven-publish")
}

group = "com.wolfscowl"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

sourceSets {
    named("main") {
        resources {
            srcDirs(
                "src/main/kotlin/ur_client/core/internal/tool/onrobot/rg/script",
                "src/main/kotlin/ur_client/core/internal/tool/onrobot/vg/script",
                "src/main/kotlin/ur_client/core/internal/tool/onrobot/tfg/script",
                "src/main/kotlin/ur_client/core/internal/arm/script",
                "src/main/kotlin/ur_client/core/internal/custom_script/script"
            )
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(8)
}


//val dokkaJavadocJar by tasks.registering(Jar::class) {
//    description = "A HTML Documentation JAR containing Dokka HTML"
//    from(tasks.dokkaGeneratePublicationHtml.flatMap { it.outputDirectory })
//    archiveClassifier.set("javadoc")
//}

val dokkaJavadocJar by tasks.registering(Jar::class) {
    description = "A HTML Documentation JAR containing Dokka HTML"
    group = "documentation"
    // Nutze 'named', um Sicherzugehen, dass V2-Modus aktiv bleibt
    val dokkaTask = tasks.named<org.jetbrains.dokka.gradle.tasks.DokkaGenerateTask>("dokkaGeneratePublicationHtml")
    from(dokkaTask.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
}

val sourcesJar by tasks.registering(Jar::class) {
    description = "Packs Kotlin sources into a JAR"
    group = "build"
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

tasks.build {
    dependsOn(dokkaJavadocJar, sourcesJar)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            artifact(dokkaJavadocJar)
            artifact(sourcesJar)
        }
    }
}

tasks.test {
    useJUnitPlatform()
}