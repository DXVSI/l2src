if [ -f mysql_pass_game.sh ]; then
		. mysql_pass_game.sh
else
		. mysql_pass_game.sh.default
fi

mysqldump --ignore-table=${DBNAME}.game_log --ignore-table=${DBNAME}.petitions --add-drop-table -h $DBHOST -u $USER --password=$PASS $DBNAME > l2p_full_backup.sql

for tab in \
		install/account_bonus.sql \
		install/account_gsdata.sql \
		install/ally_data.sql \
		install/auto_chat.sql \
		install/bans.sql \
		install/bbs_clannotice.sql \
		install/bbs_favorites.sql \
		install/bbs_mail.sql \
		install/bbs_memo.sql \
		install/bbs_sms_country.sql \
		install/bbs_sms_data.sql \
		install/bbs_teleport.sql \
		install/castle.sql \
		install/castle_damage_zones.sql \
		install/castle_door_upgrade.sql \
		install/castle_hired_guards.sql \
		install/castle_manor_procure.sql \
		install/castle_manor_production.sql \
		install/character_blocklist.sql \
		install/character_bookmarks.sql \
		install/character_effects_save.sql \
		install/character_friends.sql \
		install/character_group_reuse.sql \
		install/character_hennas.sql \
		install/character_instances.sql \
		install/character_l2top_votes \
		install/character_macroses.sql \
		install/character_mentoring.sql \
		install/character_minigame_score.sql \
		install/character_mmotop_votes.sql \
		install/character_post_friends.sql \
		install/character_premium_items.sql \
		install/character_quests.sql \
		install/character_recipebook.sql \
		install/character_shortcuts.sql \
		install/character_skills.sql \
		install/character_skills_save.sql \
		install/character_subclasses.sql \
		install/character_variables.sql \
		install/character_vote.sql \
		install/characters.sql \
		install/clan_data.sql \
		install/clan_privs.sql \
		install/clan_skills.sql \
		install/clan_subpledges.sql \
		install/clan_subpledges_skills.sql \
		install/clan_wars.sql \
		install/clanhall.sql \
		install/commission.sql \
		install/community_skillsave.sql \
		install/couples.sql \
		install/cursed_weapons.sql \
		install/custom_spawnlist.sql \
		install/dominion.sql \
		install/dominion_rewards.sql \
		install/epic_boss_spawn.sql \
		install/event_data.sql \
		install/fishing_championship.sql \
		install/fortress.sql \
		install/game_log.sql \
		install/games.sql \
		install/global_tasks.sql \
		install/heroes.sql \
		install/heroes_diary.sql \
		install/item_attributes.sql \
		install/item_auction.sql \
		install/item_auction_bid.sql \
		install/items.sql \
		install/items_delayed.sql \
		install/mail.sql \
		install/olympiad_history.sql \
		install/olympiad_nobles.sql \
		install/petitions.sql \
		install/pets.sql \
		install/raidboss_points.sql \
		install/raidboss_status.sql \
		install/residence_functions.sql \
		install/server_variables.sql \
		install/siege_clans.sql \
		install/siege_guards.sql \
		install/siege_players.sql \
		install/spawnlist.sql \
		install/tvt_teams.sql \
		install/vitality_points.sql \
		install/vote.sql \
		install/world_statistic.sql \
		install/world_statistic_monthly.sql \
		install/world_statistic_winners.sql \
		install/world_statistic_winners_monthly.sql \
	; do
		echo Loading $tab ...
		mysql -h $DBHOST -u $USER --password=$PASS -D $DBNAME < $tab
done
./upgrade.sh