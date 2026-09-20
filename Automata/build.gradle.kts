/*
 * Build-Konfiguration für das 'Automata'-Modul.
 */

plugins {
    // Gemeinsame Java-Konventionen aus buildSrc
    id("buildlogic.java-conventions")
}

dependencies {
    // Abhängigkeit zum Basis-Modul von Foomp
    api("org.quurz.foomp:base:1.0.1")

    // Nutzung des Version Catalogs (libs.versions.toml) für externe Libraries
    api(libs.org.apache.commons.commons.lang3)
    api(libs.org.checkerframework.checker.qual)

    // Test-Abhängigkeiten (nur für Tests verfügbar)
    testImplementation(libs.org.slf4j.slf4j.api)
    testImplementation(libs.ch.qos.logback.logback.classic)
}

description = "Foomp-Automata"

java {
    // Zusätzlich zum normalen JAR auch ein Javadoc-JAR erstellen
    withJavadocJar()
}
