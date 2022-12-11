plugins {
    java
    id("io.opentelemetry.instrumentation.muzzle-generation") version "1.20.2-alpha"
    id("io.opentelemetry.instrumentation.muzzle-check") version "1.20.2-alpha"
}

group "io.github.asvanberg.opentelemetry"
version "1.0"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("io.opentelemetry.instrumentation:opentelemetry-instrumentation-api:1.20.2")
    compileOnly("io.opentelemetry.javaagent:opentelemetry-javaagent-extension-api:1.20.2-alpha")
    implementation("io.opentelemetry.javaagent.instrumentation:opentelemetry-javaagent-executors:1.20.2-alpha")
    implementation("io.opentelemetry.javaagent.instrumentation:opentelemetry-javaagent-executors-bootstrap:1.20.2-alpha")

    compileOnly("com.google.auto.service:auto-service:1.0.1")
    annotationProcessor("com.google.auto.service:auto-service:1.0.1")

    // required by the muzzle-generation plugin
    add("codegen", "io.opentelemetry.javaagent:opentelemetry-javaagent-tooling:1.20.2-alpha")
}
