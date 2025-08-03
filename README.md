# Lineage 2 Rebellion Server

Серверная часть игры Lineage 2, основанная на архитектуре MMOCore.

## Описание

Этот проект представляет собой полнофункциональный сервер Lineage 2 с поддержкой:
- LoginServer (сервер авторизации)
- GameServer (игровой сервер)
- Система баз данных MariaDB
- Конфигурационные файлы на русском языке

## Архитектура

```
├── loginserver/     # Сервер авторизации
├── gameserver/      # Игровой сервер
├── src/            # Исходный код
│   ├── commons/    # Общие библиотеки
│   ├── loginserver/
│   └── gameserver/
├── lib/            # Библиотеки
├── sql/            # SQL скрипты
└── config/         # Конфигурация
```

## Требования

- Java 21+
- MariaDB/MySQL
- Ant (для сборки)

## Установка

1. Клонируйте репозиторий:
```bash
git clone https://github.com/DXVSI/l2src.git
cd l2src
```

2. Настройте базу данных:
```bash
# Создайте базу данных
mysql -u root -p
CREATE DATABASE l2jls;
CREATE DATABASE l2jgs;
```

3. Импортируйте SQL файлы:
```bash
mysql -u l2j -p l2jls < sql/login/accounts.sql
mysql -u l2j -p l2jgs < sql/install/gameserver.sql
```

4. Настройте конфигурацию:
```bash
cp loginserver/config/default/* loginserver/config/
cp gameserver/config/default/* gameserver/config/
# Отредактируйте настройки БД в config файлах
```

## Сборка

```bash
ant compile
```

## Запуск

### LoginServer:
```bash
ant run.loginserver
# или
./start_loginserver.sh
```

### GameServer:
```bash
ant run.gameserver
# или
./start_gameserver.sh
```

## Конфигурация

Основные настройки находятся в:
- `loginserver/config/loginserver.properties`
- `gameserver/config/server.properties`

## Лицензия

Этот проект предназначен для образовательных целей.

## Поддержка

Для вопросов и предложений создавайте Issues в репозитории. 