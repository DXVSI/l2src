@echo off
TITLE Lineage II Goddess of Destruction Setup
REM ######################################## Automatic updater for Lineage II Goddess of Destruction - Do not edit !!!
goto answer%ERRORLEVEL%
:answerTrue
set fastend=yes
goto upgrade_db
:answer0
set fastend=no

set userl=root
set passl=
set DBnamel=l2jls
set DBHostl=localhost

set userg=root
set passg=
set DBnameg=l2jgs
set DBHostg=localhost

set Generaltables=accounts gameservers account_log account_bonus ally_data auto_chat bans bbs_buffer bbs_buffer_allowed_buffs bbs_buffer_buffs bbs_clannotice bbs_favorites bbs_mail bbs_memo bbs_sms_country bbs_sms_data bbs_teleport castle castle_damage_zones castle_door_upgrade castle_hired_guards castle_manor_procure castle_manor_production character_blocklist character_bookmarks character_effects_save character_friends character_group_reuse character_hennas character_instances character_l2top_votes character_macroses character_mail character_minigame_score character_mmotop_votes character_post_friends character_premium_items character_quests character_recipebook character_secondary_password character_shortcuts character_skills character_skills_save character_sms_donate character_subclasses character_variables characters clan_data clan_privs clan_skills clan_subpledges clan_subpledges_skills clan_wars clanhall couples cursed_weapons dominion dominion_rewards epic_boss_spawn event_data event_hitman fishing_championship fortress game_log games global_tasks heroes heroes_diary item_attributes item_auction item_auction_bid items items_delayed mail mail_attachments olympiad_history olympiad_nobles pccafe_coupon petitions pets raidboss_points raidboss_status residence_functions server_variables seven_signs seven_signs_festival seven_signs_status siege_clans siege_guards siege_players vote
set Ignore=--ignore-table=%DBname%.game_log --ignore-table=%DBname%.petitions

REM ########################################
mysql.exe -h %DBHost% -u %user% --password=%pass% --execute="CREATE DATABASE IF NOT EXISTS %DBname%"
if not exist backup (
mkdir backup
)

REM ######################################## :main_menu
:main_menu
cls
echo.Lineage II Goddess of Destruction DB Setup
echo.
echo.### Main Menu ###
echo.
echo.(1) Install Login Server
echo.(2) Install Game Server
echo.(3) Upgrade Database
echo.(4) Install Community Board PvP
echo.(q) Quit
echo.
set button=x
set /p button=What do you want ?:
if /i %button%==1 goto Install_Login_Server_menu
if /i %button%==2 goto Install_Game_Server_menu
if /i %button%==3 goto upgrade_menu
if /i %button%==4 goto Install_Community Board PvP
if /i %button%==q goto end
goto main_menu

REM ######################################## :Install_Login_Server_menu
:Install_Login_Server_menu
cls
echo.Lineage II Goddess of Destruction DB Setup
echo.
echo.### Install Login Server ###
echo.
echo.(i) Install Login Server. Warning !!! accounts, gameservers, account_log will be deleted !!!
echo.(m) Main menu
echo.(q) Quit
echo.
set button=x
set /p button=Your choice ?:
if /i %button%==i goto Install_Login_Server
if /i %button%==m goto main_menu
if /i %button%==q goto end
goto Install_Login_Server_menu

REM ######################################## :Install_Game_Server_menu
:Install_Game_Server_menu
cls
echo.Lineage II Goddess of Destruction DB Setup
echo.
echo.### Install Game Server ###
echo.
echo.(i) Install Game Server. Warning !!! All Game Server Database will be deleted !!!
echo.(m) Main menu
echo.(q) Quit
echo.
set button=x
set /p button=Your choice ?:
if /i %button%==i goto Install_Game_Server
if /i %button%==m goto main_menu
if /i %button%==q goto end
goto Install_Game_Server_menu

REM ######################################## :upgrade_menu
:upgrade_menu
cls
echo.Lineage II Goddess of Destruction DB Setup
echo.
echo.### Upgrade Menu ###
echo.
echo.(u) Upgrade Database
echo.(m) Main menu
echo.(q) Quit
echo.
set button=x
set /p button=Your choice ?:
if /i %button%==u goto upgrade_db
if /i %button%==m goto main_menu
if /i %button%==q goto end
goto upgrade_menu

REM ######################################## :Install_communityboard_pvp
:Install_Community Board PvP
echo.Installing Community Board PvP !!!
echo.
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < communityboard_pvp/community_news.sql
echo Loading Table: community_news.sql
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < communityboard_pvp/communitybuff_allowed_buffs.sql
echo Loading Table: communitybuff_allowed_buffs.sql
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < communityboard_pvp/communitybuff_grp.sql
echo Loading Table: communitybuff_grp.sql
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < communityboard_pvp/communitybuff_grp_allowed_buffs.sql
echo Loading Table: communitybuff_grp_allowed_buffs.sql
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < communityboard_pvp/communitybuff_grp_buffs.sql
echo Loading Table: communitybuff_grp_buffs.sql
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < communityboard_pvp/comteleport.sql
echo Loading Table: comteleport.sql
echo.
echo.Community Board PvP Installed !!!
echo.
pause
goto main_menu

REM ######################################## :Install_Login_Server
:Install_Login_Server
set ctime=%TIME:~0,2%
if "%ctime:~0,1%" == " " (
set ctime=0%ctime:~1,1%
)
set ctime=%ctime%.%TIME:~3,2%.%TIME:~6,2%
echo.
echo Making a full backup into %DATE%-%ctime%_backup_full.sql
echo.
mysqldump.exe %Ignore% --add-drop-table -h %DBHostl% -u %userl% --password=%passl% %DBnamel% > backup/%DATE%-%ctime%_backup_full.sql
echo.
echo.Installing Login Server !!!
echo.
echo Loading Table: account_log
mysql.exe -h %DBHostl% -u %userl% --password=%passl% -D %DBnamel% < login/account_log.sql
echo Loading Table: accounts
mysql.exe -h %DBHostl% -u %userl% --password=%passl% -D %DBnamel% < login/accounts.sql
echo Loading Table: gameservers
mysql.exe -h %DBHostl% -u %userl% --password=%passl% -D %DBnamel% < login/gameservers.sql
echo.
echo.Login Server Installed !!!
echo.
pause
goto main_menu

REM ######################################## :Install_Game_Server
:Install_Game_Server
set ctime=%TIME:~0,2%
if "%ctime:~0,1%" == " " (
set ctime=0%ctime:~1,1%
)
set ctime=%ctime%.%TIME:~3,2%.%TIME:~6,2%
echo.
echo Making a full backup into %DATE%-%ctime%_backup_full.sql
echo.
mysqldump.exe %Ignore% --add-drop-table -h %DBHostg% -u %userg% --password=%passg% %DBnameg% > backup/%DATE%-%ctime%_backup_full.sql
echo.
echo.Installing general tables !!!
echo.
echo Loading Table: account_bonus
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/account_bonus.sql
echo Loading Table: account_gsdata
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/account_gsdata.sql
echo Loading Table: account_mentoring
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/account_mentoring.sql
echo Loading Table: ally_data
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/ally_data.sql
echo Loading Table: auto_chat
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/auto_chat.sql
echo Loading Table: bans
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/bans.sql
echo Loading Table: bbs_clannotice
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/bbs_clannotice.sql
echo Loading Table: bbs_favorites
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/bbs_favorites.sql
echo Loading Table: bbs_mail
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/bbs_mail.sql
echo Loading Table: bbs_memo
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/bbs_memo.sql
echo Loading Table: bbs_sms_country
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/bbs_sms_country.sql
echo Loading Table: bbs_sms_data
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/bbs_sms_data.sql
echo Loading Table: bbs_teleport
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/bbs_teleport.sql
echo Loading Table: castle
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/castle.sql
echo Loading Table: castle_damage_zones
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/castle_damage_zones.sql
echo Loading Table: castle_door_upgrade
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/castle_door_upgrade.sql
echo Loading Table: castle_hired_guards
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/castle_hired_guards.sql
echo Loading Table: castle_manor_procure
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/castle_manor_procure.sql
echo Loading Table: castle_manor_production
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/castle_manor_production.sql
echo Loading Table: character_blocklist
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/character_blocklist.sql
echo Loading Table: character_bookmarks
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/character_bookmarks.sql
echo Loading Table: character_effects_save
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/character_effects_save.sql
echo Loading Table: character_friends
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/character_friends.sql
echo Loading Table: character_group_reuse
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/character_group_reuse.sql
echo Loading Table: character_hennas
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/character_hennas.sql
echo Loading Table: character_instances
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/character_instances.sql
echo Loading Table: character_l2top_votes
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/character_l2top_votes.sql
echo Loading Table: character_macroses
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/character_macroses.sql
echo Loading Table: character_mentoring
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/character_mentoring.sql
echo Loading Table: character_minigame_score
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/character_minigame_score.sql
echo Loading Table: character_mmotop_votes
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/character_mmotop_votes.sql
echo Loading Table: character_post_friends
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/character_post_friends.sql
echo Loading Table: character_premium_items
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/character_premium_items.sql
echo Loading Table: character_quests
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/character_quests.sql
echo Loading Table: character_recipebook
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/character_recipebook.sql
echo Loading Table: character_shortcuts
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/character_shortcuts.sql
echo Loading Table: character_skills
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/character_skills.sql
echo Loading Table: character_skills_save
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/character_skills_save.sql
echo Loading Table: character_subclasses
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/character_subclasses.sql
echo Loading Table: character_variables
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/character_variables.sql
echo Loading Table: character_vote
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/character_vote.sql
echo Loading Table: characters
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/characters.sql
echo Loading Table: clan_data
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/clan_data.sql
echo Loading Table: clan_privs
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/clan_privs.sql
echo Loading Table: clan_skills
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/clan_skills.sql
echo Loading Table: clan_subpledges
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/clan_subpledges.sql
echo Loading Table: clan_subpledges_skills
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/clan_subpledges_skills.sql
echo Loading Table: clan_wars
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/clan_wars.sql
echo Loading Table: clanhall
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/clanhall.sql
echo Loading Table: commission
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/commission.sql
echo Loading Table: community_skillsave
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/community_skillsave.sql
echo Loading Table: couples
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/couples.sql
echo Loading Table: cursed_weapons
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/cursed_weapons.sql
echo Loading Table: custom_spawnlist
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/custom_spawnlist.sql
echo Loading Table: dominion
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/dominion.sql
echo Loading Table: dominion_rewards
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/dominion_rewards.sql
echo Loading Table: epic_boss_spawn
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/epic_boss_spawn.sql
echo Loading Table: event_data
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/event_data.sql
echo Loading Table: fishing_championship
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/fishing_championship.sql
echo Loading Table: fortress
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/fortress.sql
echo Loading Table: game_log
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/game_log.sql
echo Loading Table: games
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/games.sql
echo Loading Table: global_tasks
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/global_tasks.sql
echo Loading Table: heroes
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/heroes.sql
echo Loading Table: heroes_diary
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/heroes_diary.sql
echo Loading Table: item_attributes
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/item_attributes.sql
echo Loading Table: item_auction
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/item_auction.sql
echo Loading Table: item_auction_bid
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/item_auction_bid.sql
echo Loading Table: items
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/items.sql
echo Loading Table: items_delayed
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/items_delayed.sql
echo Loading Table: mail
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/mail.sql
echo Loading Table: olympiad_history
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/olympiad_history.sql
echo Loading Table: olympiad_nobles
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/olympiad_nobles.sql
echo Loading Table: petitions
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/petitions.sql
echo Loading Table: pets
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/pets.sql
echo Loading Table: raidboss_points
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/raidboss_points.sql
echo Loading Table: raidboss_status
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/raidboss_status.sql
echo Loading Table: residence_functions
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/residence_functions.sql
echo Loading Table: server_variables
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/server_variables.sql
echo Loading Table: siege_clans
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/siege_clans.sql
echo Loading Table: siege_guards
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/siege_guards.sql
echo Loading Table: siege_players
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/siege_players.sql
echo Loading Table: spawnlist
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/spawnlist.sql
echo Loading Table: tvt_teams
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/tvt_teams.sql
echo Loading Table: vitality_points
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/vitality_points.sql
echo Loading Table: vote
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/vote.sql
echo Loading Table: world_statistic
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/world_statistic.sql
echo Loading Table: world_statistic_monthly
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/world_statistic_monthly.sql
echo Loading Table: world_statistic_winners
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/world_statistic_winners.sql
echo Loading Table: world_statistic_winners_monthly
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < install/world_statistic_winners_monthly.sql
goto upgrade_db

REM ######################################## :upgrade_db
:upgrade_db
echo.
echo Upgrading tables !!!
echo.
echo Loading Table: fish
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < upgrade/fish.sql
echo Loading Table: fishreward
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < upgrade/fishreward.sql
echo Loading Table: four_sepulchers_spawnlist
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < upgrade/four_sepulchers_spawnlist.sql
echo Loading Table: pets_skills
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < upgrade/pets_skills.sql
echo Loading Table: pet_data
mysql.exe -h %DBHostg% -u %userg% --password=%passg% -D %DBnameg% < upgrade/pet_data.sql
echo.
echo.Upgrading complete !!!
echo.
if /I %fastend%==yes goto :EOF
pause
goto main_menu

REM ######################################## :full_backup
:full_backup
set ctime=%TIME:~0,2%
if "%ctime:~0,1%" == " " (
set ctime=0%ctime:~1,1%
)
set ctime=%ctime%.%TIME:~3,2%.%TIME:~6,2%
echo.
echo Making a full backup into %DATE%-%ctime%_backup_full.sql
echo.
mysqldump.exe %Ignore% --add-drop-table -h %DBHost% -u %user% --password=%pass% %DBname% > backup/%DATE%-%ctime%_backup_full.sql
goto end

REM ######################################## :general_backup
:general_backup
set ctime=%TIME:~0,2%
if "%ctime:~0,1%" == " " (
set ctime=0%ctime:~1,1%
)
set ctime=%ctime%.%TIME:~3,2%.%TIME:~6,2%
echo.
echo Making a general tables backup into %DATE%-%ctime%_backup_general.sql
echo.
mysqldump.exe %Ignore% --add-drop-table -h %DBHost% -u %user% --password=%pass% %DBname% %Generaltables% > backup/%DATE%-%ctime%_backup_general.sql
goto end

REM ######################################## :restore_DB
:restore_DB
if not exist backup/%filename% (
echo.
echo.File not found !
echo.
pause
goto restore_menu
)
cls
echo.
echo.Restore from file %filename% !
echo.
pause
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < backup/%filename%
goto end

REM ######################################## :not_working_now
:not_working_now
echo.
echo Not working NOW !!!
echo.
pause
goto main_menu

REM ######################################## :end
:end
