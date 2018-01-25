package ru.foxseasha.tban.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import ru.foxseasha.tban.Ban;
import ru.foxseasha.tban.BanManager;
import ru.foxseasha.tban.Main;

public class MySQL
{
    public static Connection connection;
    
    static {
        MySQL.connection = null;
    }
    
    public static void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            MySQL.connection = DriverManager.getConnection("jdbc:mysql://" + Main.config.getString("mysql.host") + ":" + Main.config.getInt("mysql.port") + "/" + Main.config.getString("mysql.database") + "?useUnicode=true&characterEncoding=UTF-8&" + "user=" + Main.config.getString("mysql.username") + "&password=" + Main.config.getString("mysql.password"));
            executeSync("CREATE TABLE IF NOT EXISTS `tban` (`id` int NOT NULL AUTO_INCREMENT, `player` varchar(16) NOT NULL, `type` varchar(16) NOT NULL, `owner` varchar(16) NOT NULL, `time` varchar(16) NOT NULL, `expire` varchar(16) NOT NULL, `reason` varchar(255) NOT NULL, PRIMARY KEY (`id`), UNIQUE KEY `uniq` (`player`)) DEFAULT CHARSET=utf8 AUTO_INCREMENT=0");
            Logger.info("\u0421\u043e\u0435\u0434\u0438\u043d\u0435\u043d\u0438\u0435 \u0441 \u0431\u0430\u0437\u043e\u0439 \u0434\u0430\u043d\u043d\u044b\u0445 \u0443\u0441\u043f\u0435\u0448\u043d\u043e \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d\u043e.");
        }
        catch (Exception e) {
            Logger.error("MySQL ERROR: " + e.getMessage());
        }
    }
    
    public static boolean hasConnected() {
        try {
            return !MySQL.connection.isClosed();
        }
        catch (Exception e) {
            return false;
        }
    }
    
    public static String strip(String str) {
        str = str.replaceAll("<[^>]*>", "");
        str = str.replace("\\", "\\\\");
        str = str.trim();
        return str;
    }
    
    public static void execute(final String query) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, (Runnable)new Runnable() {
            @Override
            public void run() {
                try {
                    if (!MySQL.hasConnected()) {
                        MySQL.connect();
                    }
                    final Statement st = MySQL.connection.createStatement();
                    st.execute(MySQL.strip(query));
                    st.close();
                    Logger.debug(MySQL.strip(query));
                }
                catch (Exception e) {
                    Logger.error(e.getMessage());
                }
            }
        });
    }
    
    public static void executeSync(final String query) {
        try {
            if (!hasConnected()) {
                connect();
            }
            final Statement st = MySQL.connection.createStatement();
            st.execute(strip(query));
            st.close();
            Logger.debug(strip(query));
        }
        catch (Exception e) {
            Logger.error(e.getMessage());
        }
    }
    
    public static void getBans() {
        BanManager.bans.clear();
        try {
            int i = 0;
            final ResultSet bans = executeQuery("SELECT * FROM `tban`");
            while (bans.next()) {
                try {
                    boolean delete = false;
                    final BanType type = BanType.getByName(bans.getString("type"));
                    if (type == null) {
                        delete = true;
                    }
                    if (bans.getLong("expire") > 0L && bans.getLong("expire") < System.currentTimeMillis()) {
                        delete = true;
                    }
                    if (delete) {
                        execute("DELETE FROM `tban` WHERE `player`='" + bans.getString("player") + "'");
                    }
                    else {
                        final Ban b = new Ban(bans.getString("player").toLowerCase(), BanType.getByName(bans.getString("type")), bans.getString("owner"), bans.getLong("time"), bans.getLong("expire"), bans.getString("reason"));
                        BanManager.bans.put(bans.getString("player"), b);
                        ++i;
                    }
                }
                catch (Exception e) {
                    Logger.error(e.getMessage());
                }
            }
            Logger.info("\u0417\u0430\u0433\u0440\u0443\u0436\u0435\u043d\u043e " + i + " \u0431\u0430\u043d\u043e\u0432.");
        }
        catch (Exception e2) {
            Logger.error(e2.getMessage());
        }
    }
    
    public static void getBansAsync() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(Main.plugin, (Runnable)new Runnable() {
            @Override
            public void run() {
                BanManager.bans.clear();
                try {
                    final ResultSet bans = MySQL.executeQuery("SELECT * FROM `tban`");
                    while (bans.next()) {
                        try {
                            boolean delete = false;
                            final BanType type = BanType.getByName(bans.getString("type"));
                            if (type == null) {
                                delete = true;
                            }
                            if (bans.getLong("expire") > 0L && bans.getLong("expire") < System.currentTimeMillis()) {
                                delete = true;
                            }
                            if (delete) {
                                MySQL.execute("DELETE FROM `tban` WHERE `player`='" + bans.getString("player") + "'");
                            }
                            else {
                                final Ban b = new Ban(bans.getString("player").toLowerCase(), BanType.getByName(bans.getString("type")), bans.getString("owner"), bans.getLong("time"), bans.getLong("expire"), bans.getString("reason"));
                                BanManager.bans.put(bans.getString("player"), b);
                            }
                        }
                        catch (Exception e) {
                            Logger.error(e.getMessage());
                        }
                    }
                    for (final Player pl : Bukkit.getOnlinePlayers()) {
                    	
                        final Ban b = BanManager.getBanByPlayer(pl.getName());
                        if (b != null && b.getType() != BanType.MUTE) {
                            pl.kickPlayer(ChatColor.RED + "\u0412\u044b \u0431\u044b\u043b\u0438 \u0437\u0430\u0431\u0430\u043d\u0435\u043d\u044b \u0438\u0437\u0432\u043d\u0435.");
                        }
                    }
                }
                catch (Exception e2) {
                    Logger.error(e2.getMessage());
                }
            }
        }, 0L, 100L);
    }
    
    public static ResultSet executeQuery(final String query) throws Exception {
        if (!hasConnected()) {
            connect();
        }
        Logger.debug(strip(query));
        final Statement st = MySQL.connection.createStatement();
        try {
            return MySQL.connection.createStatement().executeQuery(strip(query));
        }
        finally {
            st.close();
        }
    }
    
    public static void disconnect() {
        try {
            if (MySQL.connection != null) {
                MySQL.connection.close();
            }
        }
        catch (Exception ex) {}
    }
}
