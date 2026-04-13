/*
 * Build-Konfiguration für das 'Automata'-Modul.
 * Enthält zusätzlich Konfigurationen für AsciiDoctor (Dokumentation).
 */

plugins {
    // Gemeinsame Java-Konventionen
    id("buildlogic.java-conventions")
}

dependencies {
    // Abhängigkeit zum Basis-Modul
    api("org.quurz.foomp:base:0.1.0-SNAPSHOT")

    api(libs.org.apache.commons.commons.lang3)
    api(libs.org.checkerframework.checker.qual)

    testImplementation(libs.org.slf4j.slf4j.api)
    testImplementation(libs.ch.qos.logback.logback.classic)
}

description = "Foomp-Automata"

java {
    withJavadocJar()
}

/*
 * Task zum Installieren der npm-Abhängigkeiten in docs-site.
 */
val npmInstallDocs = tasks.register<Exec>("npmInstallDocs") {
    group = "documentation"
    description = "Installiert npm-Abhängigkeiten für die Astro-Seite."
    workingDir = file("docs-site")
    commandLine("npm", "install")
    inputs.file("docs-site/package.json")
    outputs.dir("docs-site/node_modules")
}

/*
 * Task zum Bauen der Astro-Seite.
 */
val buildAstroDocs = tasks.register<Exec>("buildAstroDocs") {
    group = "documentation"
    description = "Baut die Astro Starlight Dokumentation."
    dependsOn(npmInstallDocs, "assembleDocsForStarlight")
    workingDir = file("docs-site")
    commandLine("npm", "run", "build")
    inputs.dir("docs-site/src")
    inputs.dir("docs-site/public")
    outputs.dir("docs-site/dist")
}

// Hilfsvariable für den Zielordner der Projekthomepage (Site)
val moduleSiteDir = layout.buildDirectory.dir("site")

/*
 * Eigener Task 'moduleSite', der verschiedene Dokumentationen (Docs, Javadoc, Coverage)
 * an einem zentralen Ort zusammenführt.
 */
tasks.register<Copy>("moduleSite") {
    group = "documentation"
    description = "Erzeugt die Modul-Site (Astro, JavaDoc, Coverage)."

    // Dieser Task startet automatisch alle benötigten Generierungsschritte
    dependsOn(
        buildAstroDocs,
        "javadoc",
        "test",
        "jacocoTestReport"
    )

    into(moduleSiteDir)

    // Kopieren der generierten Astro-Seite
    from(file("docs-site/dist"))

    // Kopieren des JaCoCo-Abdeckungsberichts
    from(layout.buildDirectory.dir("reports/jacoco/test/html")) {
        into("jacoco")
    }
}
