plugins {
    java
    application
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    // Зависимость от commons
    implementation(project(":commons"))
    
    // База данных
    implementation("org.mariadb.jdbc:mariadb-java-client:3.5.0")
    
    // Логирование  
    implementation("org.slf4j:slf4j-api:1.7.36")
    implementation("org.slf4j:slf4j-log4j12:1.7.36")
    implementation("log4j:log4j:1.2.17")
    
    // Сеть
    implementation("io.netty:netty-all:4.1.100.Final")
}

// Настройка путей исходников
sourceSets {
    main {
        java {
            setSrcDirs(listOf("."))
        }
        // resources {
        //     setSrcDirs(listOf("../../../loginserver/src/main/resources"))
        // }
    }
}

// Главный класс приложения
application {
    mainClass.set("org.mmocore.loginserver.LoginServer")
}

// Задача для запуска loginserver
tasks.register<JavaExec>("runLoginServer") {
    group = "application"
    description = "Run LoginServer"
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("org.mmocore.loginserver.LoginServer")
    workingDir = file("../../loginserver/dist")
}