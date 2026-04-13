/*
 * Build-Konfiguration für das 'Automata'-Modul.
 * Enthält zusätzlich Konfigurationen für AsciiDoctor (Dokumentation).
 */

plugins {
    // Gemeinsame Java-Konventionen
    id("buildlogic.java-conventions")
    // Plugin für die Umwandlung von AsciiDoc (.adoc) in HTML/PDF
    id("org.asciidoctor.jvm.convert") version "4.0.5"
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
 * Konfiguration des Asciidoctor-Tasks.
 * Legt fest, wo die Quelldateien liegen und wohin das Ergebnis geschrieben wird.
 */
tasks.named<org.asciidoctor.gradle.jvm.AsciidoctorTask>("asciidoctor") {
    // Quellverzeichnis für .adoc Dateien
    setSourceDir(file("src/site/adoc"))
    // Zielverzeichnis für generiertes HTML
    setOutputDir(layout.buildDirectory.dir("docs/asciidoc").get().asFile)
}

// Hilfsvariable für den Zielordner der Projekthomepage (Site)
val moduleSiteDir = layout.buildDirectory.dir("site")

/*
 * Eigener Task 'moduleSite', der verschiedene Dokumentationen (Docs, Javadoc, Coverage)
 * an einem zentralen Ort zusammenführt.
 */
tasks.register<Copy>("moduleSite") {
    group = "documentation"
    description = "Erzeugt die Modul-Site (Asciidoc, JavaDoc, Coverage)."

    // Dieser Task startet automatisch alle benötigten Generierungsschritte
    dependsOn(
        "asciidoctor",
        "javadoc",
        "test",
        "jacocoTestReport"
    )

    into(moduleSiteDir)

    // Kopieren der generierten Asciidoc-HTML-Dateien
    from(layout.buildDirectory.dir("docs/asciidoc")) {
        into("docs")
    }
    // Kopieren der JavaDoc-Dokumentation
    from(layout.buildDirectory.dir("docs/javadoc")) {
        into("javadoc")
    }
    // Kopieren des JaCoCo-Abdeckungsberichts
    from(layout.buildDirectory.dir("reports/jacoco/test/html")) {
        into("jacoco")
    }
}
