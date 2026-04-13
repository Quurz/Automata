/*
 * Dies ist ein Convention-Plugin. Es kapselt gemeinsame Build-Logik,
 * die in mehreren Submodulen (Base, Automata, etc.) wiederverwendet wird.
 * Durch 'plugins { id("buildlogic.java-conventions") }' in den Submodulen
 * wird der gesamte hier definierte Code dort aktiv.
 */

import org.gradle.api.tasks.testing.Test
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.external.javadoc.StandardJavadocDocletOptions
import org.gradle.api.tasks.SourceSetContainer

plugins {
    // Standard Java-Library-Support (bietet 'api', 'implementation' etc.)
    `java-library`
    // Code-Coverage Tool
    jacoco
    // Ermöglicht das Veröffentlichen von Artefakten (z.B. nach MavenLocal)
    `maven-publish`
}

// Zentrale Paket-Quellen für Abhängigkeiten
repositories {
    mavenCentral()
    mavenLocal()
}

// Projektweite Metadaten
group = "org.quurz.foomp"
version = "0.1.0-SNAPSHOT"

// Java-Version festlegen (hier Java 25)
java.sourceCompatibility = JavaVersion.VERSION_25
java.targetCompatibility = JavaVersion.VERSION_25

java {
    // Automatisch ein JAR mit den Quellcodedateien erstellen
    withSourcesJar()
}

jacoco {
    // Version des Jacoco-Agents
    toolVersion = "0.8.13"
}

/*
 * Konfigurationen (Configurations) sind Gruppen von Abhängigkeiten.
 * Hier erstellen wir eigene Gruppen für spezielle Werkzeuge.
 */
// Für das UML-Diagramm-Tool in Javadoc
val umlDoclet: Configuration by configurations.creating

// Für das Checker Framework (statische Analyse)
val checkerFramework: Configuration by configurations.creating

/*
 * Zentrale Abhängigkeiten, die JEDES Modul erhält, das dieses Plugin nutzt.
 */
dependencies {
    // Werkzeuge für den Build-Prozess (in den oben erstellten Configurations)
    checkerFramework("org.checkerframework:checker:3.48.1")
    umlDoclet("nl.talsmasoftware:umldoclet:2.2.4")

    // Unit-Testing Framework: JUnit 5 (Jupiter)
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.0")
    // Benötigt für die Ausführung von JUnit 5 Tests in Gradle
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.11.0")

    // Assertion-Library für flüssigere Tests
    testImplementation("org.assertj:assertj-core:3.26.0")
    // Mocking-Framework
    testImplementation("org.mockito:mockito-core:5.14.1")
    testImplementation("org.mockito:mockito-junit-jupiter:5.14.1")
}

/*
 * Konfiguration aller Test-Tasks.
 * .configureEach stellt sicher, dass alle Instanzen vom Typ 'Test' angepasst werden.
 */
tasks.withType<Test>().configureEach {
    // JUnit 5 verwenden
    useJUnitPlatform()
    // Schöne Ausgabe in der Konsole
    testLogging {
        events("PASSED", "FAILED", "SKIPPED")
    }
    // Nach jedem Testlauf automatisch den Coverage-Report starten
    finalizedBy("jacocoTestReport")
}

// Konfiguration der Jacoco-Reports
tasks.withType<JacocoReport>().configureEach {
    // Der Report benötigt die Ergebnisse aus dem Test-Run
    dependsOn("test")

    reports {
        xml.required.set(true)
        csv.required.set(false)
        html.required.set(true) // HTML-Report zum Anschauen im Browser
    }
}

// Grundeinstellung für das Veröffentlichen von Maven-Artefakten
publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

/*
 * Einstellungen für den Java-Compiler (javac)
 */
tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    // Limits für Fehlermeldungen hochsetzen
    options.compilerArgs.addAll(listOf("-Xmaxerrs", "10000", "-Xmaxwarns", "10000"))

    // Hinweis: Checker Framework ist aktuell deaktiviert, um Build-Probleme unter Java 25 zu vermeiden.
    // Die Annotationen (@Pure, @NonNull) funktionieren in der IDE trotzdem.
    /*
    options.annotationProcessorPath = checkerFramework
    options.compilerArgs.addAll(listOf(
        "-processor", "org.checkerframework.checker.nullness.NullnessChecker",
        "-Xlint:-processing",
        "-Awarns"
    ))
    options.isFork = true
    options.forkOptions.jvmArgs?.addAll(listOf(
        "-Xbootclasspath/a:${checkerFramework.asPath}",
        "--add-exports", "jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED",
        ...
    ))
    */
}

// Basis-Javadoc-Einstellungen
tasks.withType<Javadoc>().configureEach {
    options.encoding = "UTF-8"
    // Javadoc-Linting (strenge Prüfung) aktuell deaktiviert
    (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
}

/*
 * Erweiterte Javadoc-Konfiguration für UML-Diagramme
 */
tasks.withType<Javadoc>().configureEach {
    val docletOptions = options as StandardJavadocDocletOptions

    // Alle Java-Dateien des Moduls einbeziehen
    source = project.extensions.getByType<SourceSetContainer>()["main"].allJava

    // Den UMLDoclet-Pfad und die Klasse setzen
    docletOptions.docletpath = umlDoclet.files.toList()
    docletOptions.doclet = "nl.talsmasoftware.umldoclet.UMLDoclet"

    // Optional: Statische Ressourcen (Bilder, CSS) aus src/main/javadoc kopieren
    val javadocResources = project.file("src/main/javadoc")
    if (javadocResources.exists()) {
        inputs.dir(javadocResources)
        doLast {
            copy {
                from(javadocResources)
                into(destinationDir!!)
            }
        }
    }
}
