#!/bin/bash

echo "Запуск GameServer..."

# Переходим в папку с gameserver
cd gameserver

# Запускаем gameserver
java -server \
    -Dfile.encoding=UTF-8 \
    -Xms1024m \
    -Xmx2048m \
    -cp "../lib/*:../build/commons:../build/gameserver:config" \
    org.mmocore.gameserver.GameServer

echo "GameServer остановлен."