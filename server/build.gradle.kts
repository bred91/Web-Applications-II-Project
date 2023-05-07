import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.0.5"
	id("io.spring.dependency-management") version "1.1.0"
	kotlin("jvm") version "1.7.22"
	kotlin("plugin.spring") version "1.7.22"
	kotlin("plugin.jpa") version "1.7.22"
	id("org.jetbrains.kotlin.plugin.noarg") version "1.8.20"
	id("org.jetbrains.kotlin.plugin.allopen") version "1.8.20"
	id("com.google.cloud.tools.jib") version "3.3.1"
	id("io.freefair.lombok") version "8.0.1"
}

group = "it.polito"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
	testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	testImplementation("io.mockk:mockk:1.13.4")
	testImplementation ("org.testcontainers:junit-jupiter:1.16.3")
	testImplementation("org.testcontainers:postgresql:1.16.3")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(module = "mockito-core")
	}
}

dependencyManagement {
	imports {
		mavenBom("org.testcontainers:testcontainers-bom:1.16.3")
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

jib{
	to{
		image= "wa2-g15"
		tags= setOf("latest")
	}
	container{
		creationTime
		ports = listOf("8080")
	}
}
