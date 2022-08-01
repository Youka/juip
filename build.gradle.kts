// Used java version
val javaVersion = JavaVersion.current()
if (javaVersion.ordinal >= JavaVersion.VERSION_16.ordinal) {
    throw UnsupportedOperationException("Javac plugins with write access got broken by JEP-403 (https://openjdk.org/jeps/403) for version 16+!")
}
val isOldJava = javaVersion.ordinal <= JavaVersion.VERSION_1_8.ordinal

// Project information
group = "io.youka"
version = "1.0.0-jdk${javaVersion.majorVersion}"

// Load plugins
plugins {
    java
    checkstyle
    jacoco
    `maven-publish`
}

// Load dependencies
repositories {
    mavenCentral()
}
dependencies {
    val junitVersion = "5.9.0"
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    if (isOldJava) {
        val javaHome = System.getProperty("java.home") ?: throw IllegalStateException("Java home not defined!")
        compileOnly(files("$javaHome/../lib/tools.jar"))
    }
}

// Configure plugins & tasks
java {
    sourceCompatibility = javaVersion
    targetCompatibility = sourceCompatibility
    withSourcesJar()
    withJavadocJar()
}
checkstyle {
    configDirectory.set(rootDir)
}
tasks {
    compileJava {
        if (!isOldJava) {
            options.compilerArgs.addAll(sequenceOf(
                "--add-exports=jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED",
                "--add-exports=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED",
                "--add-exports=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED"
            ))
        }
    }
    compileTestJava {
        options.compilerArgs.addAll(sequenceOf(
            // See 'main/resources/META-INF/services/com.sun.source.util.Plugin' for plugin class and its 'getName()' implementation
            "-Xplugin:juip"
        ))
    }
    test {
        useJUnitPlatform()
    }
    jacocoTestReport {
        reports {
            xml.required.set(true)
        }
    }
}