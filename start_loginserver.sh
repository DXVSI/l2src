#!/bin/bash

echo "Запуск LoginServer..."

# Переходим в папку с loginserver
cd loginserver

# Запускаем loginserver
java -server \
    -Dfile.encoding=UTF-8 \
    -Xms256m \
    -Xmx512m \
    -cp "../lib/*:../build/commons:../build/loginserver:config" \
    org.mmocore.loginserver.LoginServer

echo "LoginServer остановлен."