/*
 * Die settings.gradle.kts ist der Einstiegspunkt für Gradle.
 * Hier wird der Projektname definiert und festgelegt, welche Submodule (Projekte)
 * Teil dieses Multi-Project-Builds sind.
 */

// Name des Hauptprojekts (Root)
rootProject.name = "Automata"

/*
 * Definition der Submodule. Jedes Modul wird mit 'include' angemeldet.
 * Der Doppelpunkt ':' ist der Pfad-Trennner in Gradle-Projektstrukturen.
 */
include(":automata")

/*
 * Standardmäßig erwartet Gradle die Submodule in Ordnern, die genau so heißen wie das Projekt.
 * Falls eine verschachtelte Ordnerstruktur genutzt wird, müssen die Pfade zu den Projektverzeichnissen
 * manuell zugewiesen werden.
 */
project(":automata").projectDir = file("Automata")
