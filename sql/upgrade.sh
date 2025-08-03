if [ -f mysql_pass_game.sh ]; then
		. mysql_pass_game.sh
else
		. mysql_pass_game.sh.default
fi

mysqldump --ignore-table=${DBNAME}.game_log --ignore-table=${DBNAME}.petitions --add-drop-table -h $DBHOST -u $USER --password=$PASS $DBNAME > ${DBNAME}_upgrade_`date +%Y-%m-%d_%H:%M:%S`.sql

for tab in \
		upgrade/fish.sql \
		upgrade/fishreward.sql \
		upgrade/four_sepulchers_spawnlist.sql \
		upgrade/pets_skills.sql \
	; do
		echo Loading $tab ...
		mysql -h $DBHOST -u $USER --password=$PASS -D $DBNAME < $tab
done