if [ -f mysql_pass_login.sh ]; then
		. mysql_pass_login.sh
else
		. mysql_pass_login.sh.default
fi

mysqldump --ignore-table=${DBNAME}.game_log --ignore-table=${DBNAME}.petitions --add-drop-table -h $DBHOST -u $USER --password=$PASS $DBNAME > l2p_full_backup.sql

for tab in \
		login/account_log.sql \
		login/accounts.sql \
		login/gameservers.sql \
	; do
		echo Loading $tab ...
		mysql -h $DBHOST -u $USER --password=$PASS -D $DBNAME < $tab
done