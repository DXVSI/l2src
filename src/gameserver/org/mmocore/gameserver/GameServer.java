package org.mmocore.gameserver;

import net.sf.ehcache.CacheManager;
import org.mmocore.commons.lang.StatsUtils;
import org.mmocore.commons.listener.Listener;
import org.mmocore.commons.listener.ListenerList;
import org.mmocore.commons.net.nio.impl.SelectorThread;
import org.mmocore.commons.versioning.Version;
import org.mmocore.gameserver.cache.CrestCache;
import org.mmocore.gameserver.dao.CharacterDAO;
import org.mmocore.gameserver.dao.ItemsDAO;
import org.mmocore.gameserver.data.BoatHolder;
import org.mmocore.gameserver.data.xml.Parsers;
import org.mmocore.gameserver.data.xml.holder.EventHolder;
import org.mmocore.gameserver.data.xml.holder.ResidenceHolder;
import org.mmocore.gameserver.data.xml.holder.StaticObjectHolder;
import org.mmocore.gameserver.database.DatabaseFactory;
import org.mmocore.gameserver.geodata.GeoEngine;
import org.mmocore.gameserver.handler.admincommands.AdminCommandHandler;
import org.mmocore.gameserver.handler.items.ItemHandler;
import org.mmocore.gameserver.handler.usercommands.UserCommandHandler;
import org.mmocore.gameserver.handler.voicecommands.VoicedCommandHandler;
import org.mmocore.gameserver.idfactory.IdFactory;
import org.mmocore.gameserver.instancemanager.*;
import org.mmocore.gameserver.instancemanager.commission.CommissionShopManager;
import org.mmocore.gameserver.instancemanager.games.FishingChampionShipManager;
import org.mmocore.gameserver.instancemanager.games.LotteryManager;
import org.mmocore.gameserver.instancemanager.games.MiniGameScoreManager;
import org.mmocore.gameserver.instancemanager.itemauction.ItemAuctionManager;
import org.mmocore.gameserver.instancemanager.naia.NaiaCoreManager;
import org.mmocore.gameserver.instancemanager.naia.NaiaTowerManager;
import org.mmocore.gameserver.listener.GameListener;
import org.mmocore.gameserver.listener.game.OnShutdownListener;
import org.mmocore.gameserver.listener.game.OnStartListener;
import org.mmocore.gameserver.loginservercon.LoginServerCommunication;
import org.mmocore.gameserver.model.AutoChatHandler;
import org.mmocore.gameserver.model.World;
import org.mmocore.gameserver.model.entity.Hero;
import org.mmocore.gameserver.model.entity.MonsterRace;
import org.mmocore.gameserver.model.entity.olympiad.Olympiad;
import org.mmocore.gameserver.model.items.etcitems.AttributeStoneManager;
import org.mmocore.gameserver.model.items.etcitems.EnchantScrollManager;
import org.mmocore.gameserver.model.items.etcitems.LifeStoneManager;
import org.mmocore.gameserver.model.party.PartySubstitute;
import org.mmocore.gameserver.model.systems.VitalityBooty;
import org.mmocore.gameserver.network.GameClient;
import org.mmocore.gameserver.network.GamePacketHandler;
import org.mmocore.gameserver.network.telnet.TelnetServer;
import org.mmocore.gameserver.scripts.Scripts;
import org.mmocore.gameserver.tables.*;
import org.mmocore.gameserver.taskmanager.ItemsAutoDestroy;
import org.mmocore.gameserver.taskmanager.TaskManager;
import org.mmocore.gameserver.taskmanager.tasks.RestoreOfflineTraders;
import org.mmocore.gameserver.utils.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.InetAddress;
import java.net.ServerSocket;

public class GameServer {
    public static final int LOGIN_SERVER_PROTOCOL = 2;
    private static final Logger _log = LoggerFactory.getLogger(GameServer.class);
    public static GameServer _instance;
    private final SelectorThread<GameClient> _selectorThreads[];
    private final GameServerListenerList _listeners;
    private Version version;
    private TelnetServer statusServer;
    private int _serverStarted;

    @SuppressWarnings("unchecked")
    public GameServer() throws Exception {
        long serverLoadStart = System.currentTimeMillis();
        _instance = this;
        _serverStarted = time();
        _listeners = new GameServerListenerList();

        new File("./log/").mkdir();

        version = new Version(GameServer.class);

        _log.info("=================================================");
        _log.info("Revision: ................ " + version.getRevisionNumber());
        _log.info("Build date: .............. " + version.getBuildDate());
        _log.info("Compiler version: ........ " + version.getBuildJdk());
        _log.info("=================================================");

        // Initialize config
        Config.load();
        // Check binding address
        checkFreePorts();
        // Initialize database
        Class.forName(Config.DATABASE_DRIVER).newInstance();
        DatabaseFactory.getInstance().getConnection().close();

        IdFactory _idFactory = IdFactory.getInstance();
        if (!_idFactory.isInitialized()) {
            _log.error("Could not read object IDs from DB. Please Check Your Data.");
            throw new Exception("Could not initialize the ID factory");
        }

        CacheManager.getInstance();

        ThreadPoolManager.getInstance();

        /************** L2WT **************/        /*
         * Лучше прогружать мененджеры итемов ДО скриптов так как искрипты используют таблицы этих мененжеров Иначе - скриптам вернутся пустые
		 * таблицы!!!
		 */
        AttributeStoneManager.load();
        EnchantScrollManager.load();
        LifeStoneManager.load();
        VitalityBooty.load();
        /************** L2WT **************/

        Scripts.getInstance();

        GeoEngine.loadGeo();

        Strings.reload();

        GameTimeController.getInstance();

        World.init();

        Parsers.parseAll();

        ItemsDAO.getInstance();

        CrestCache.getInstance();

        CharacterDAO.getInstance();

        ClanTable.getInstance();

        SkillTreeTable.getInstance();

        AugmentationData.getInstance();

        PetSkillsTable.getInstance();

        EnchantHPBonusTable.getInstance();

        ItemAuctionManager.getInstance();

        SpawnManager.getInstance().spawnAll();

        StaticObjectHolder.getInstance().spawnAll();

        RaidBossSpawnManager.getInstance();

        Scripts.getInstance().init();

        Announcements.getInstance();

        LotteryManager.getInstance();

        PlayerMessageStack.getInstance();

        if (Config.AUTODESTROY_ITEM_AFTER > 0)
            ItemsAutoDestroy.getInstance();

        MonsterRace.getInstance();

        AutoChatHandler _autoChatHandler = AutoChatHandler.getInstance();
        _log.info("AutoChatHandler: Loaded " + _autoChatHandler.size() + " handlers in total.");

        if (Config.ENABLE_OLYMPIAD) {
            Olympiad.load();
            Hero.getInstance();
        }

        PetitionManager.getInstance();

        CursedWeaponsManager.getInstance();

        if (!Config.ALLOW_WEDDING) {
            CoupleManager.getInstance();
            _log.info("CoupleManager initialized");
        }

        ItemHandler.getInstance();

        AdminCommandHandler.getInstance().log();
        UserCommandHandler.getInstance().log();
        VoicedCommandHandler.getInstance().log();

        TaskManager.getInstance();

        _log.info("=[Events]=========================================");
        ResidenceHolder.getInstance().callInit();
        EventHolder.getInstance().callInit();
        _log.info("==================================================");

        BoatHolder.getInstance().spawnAll();
        CastleManorManager.getInstance();

        Runtime.getRuntime().addShutdownHook(Shutdown.getInstance());

        _log.info("IdFactory: Free ObjectID's remaining: " + IdFactory.getInstance().size());

        CoupleManager.getInstance();

        if (Config.ALT_FISH_CHAMPIONSHIP_ENABLED)
            FishingChampionShipManager.getInstance();

        HellboundManager.getInstance();

        NaiaTowerManager.getInstance();
        NaiaCoreManager.getInstance();

        SoDManager.getInstance();
        SoIManager.getInstance();
        SoHManager.getInstance();
        HarnakUndegroundManager.getInstance();

        MiniGameScoreManager.getInstance();

        /************** L2WT **************/
        if (Config.L2TOP_MANAGER_ENABLED)
            L2TopManager.getInstance();
        if (Config.MMO_TOP_MANAGER_ENABLED)
            MMOTopManager.getInstance();
        CommissionShopManager.getInstance();
        AwakingManager.getInstance();
        PartySubstitute.getInstance();
        /************** L2WT **************/
        ccpGuard.Protection.Init();
        ArcanManager.getInstance();
        WorldStatisticsManager.getInstance();
        DimensionalRiftManager.getInstance();
        
        // Only while need
        //new DeadlockDetector().start(); // for test

        SubClassTable.getInstance();

        if (Config.GARBAGE_COLLECTOR_INTERVAL > 0)
            Class.forName(GarbageCollector.class.getName());

        Shutdown.getInstance().schedule(Config.RESTART_AT_TIME, Shutdown.RESTART);

        _log.info("GameServer Started");
        _log.info("Maximum Numbers of Connected Players: " + Config.MAXIMUM_ONLINE_USERS);

        GamePacketHandler gph = new GamePacketHandler();

        InetAddress serverAddr = Config.GAMESERVER_HOSTNAME.equalsIgnoreCase("*") ? null : InetAddress.getByName(Config.GAMESERVER_HOSTNAME);

        _selectorThreads = new SelectorThread[Config.PORTS_GAME.length];
        for (int i = 0; i < Config.PORTS_GAME.length; i++) {
            _selectorThreads[i] = new SelectorThread<GameClient>(Config.SELECTOR_CONFIG, gph, gph, gph, null);
            _selectorThreads[i].openServerSocket(serverAddr, Config.PORTS_GAME[i]);
            _selectorThreads[i].start();
        }

        LoginServerCommunication.getInstance().start();

        if (Config.SERVICES_OFFLINE_TRADE_RESTORE_AFTER_RESTART)
            ThreadPoolManager.getInstance().schedule(new RestoreOfflineTraders(), 30000L);

        getListeners().onStart();

        if (Config.IS_TELNET_ENABLED)
            statusServer = new TelnetServer();
        else
            _log.info("Telnet server is currently disabled.");

        _log.info("=================================================");
        String memUsage = String.valueOf(StatsUtils.getMemUsage());
        for (String line : memUsage.split("\n"))
            _log.info(line);
        _log.info("=================================================");
        long serverLoadEnd = System.currentTimeMillis();
        _log.info("Server Loaded in " + ((serverLoadEnd - serverLoadStart) / 1000) + " seconds");

    }

    public static GameServer getInstance() {
        return _instance;
    }

    public static void checkFreePorts() {
        boolean binded = false;
        while (!binded)
            for (int PORT_GAME : Config.PORTS_GAME)
                try {
                    ServerSocket ss;
                    if (Config.GAMESERVER_HOSTNAME.equalsIgnoreCase("*"))
                        ss = new ServerSocket(PORT_GAME);
                    else
                        ss = new ServerSocket(PORT_GAME, 50, InetAddress.getByName(Config.GAMESERVER_HOSTNAME));
                    ss.close();
                    binded = true;
                } catch (Exception e) {
                    _log.warn("Port " + PORT_GAME + " is allready binded. Please free it and restart server.");
                    binded = false;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {
                    }
                }
    }

    public static void main(String[] args) throws Exception {
        new GameServer();
    }

    public SelectorThread<GameClient>[] getSelectorThreads() {
        return _selectorThreads;
    }

    public int time() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    public int uptime() {
        return time() - _serverStarted;
    }

    public GameServerListenerList getListeners() {
        return _listeners;
    }

    public <T extends GameListener> boolean addListener(T listener) {
        return _listeners.add(listener);
    }

    public <T extends GameListener> boolean removeListener(T listener) {
        return _listeners.remove(listener);
    }

    public Version getVersion() {
        return version;
    }

    public TelnetServer getStatusServer() {
        return statusServer;
    }

    public class GameServerListenerList extends ListenerList<GameServer> {
        public void onStart() {
            for (Listener<GameServer> listener : getListeners())
                if (OnStartListener.class.isInstance(listener))
                    ((OnStartListener) listener).onStart();
        }

        public void onShutdown() {
            for (Listener<GameServer> listener : getListeners())
                if (OnShutdownListener.class.isInstance(listener))
                    ((OnShutdownListener) listener).onShutdown();
        }
    }
}