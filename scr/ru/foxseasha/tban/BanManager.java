package ru.foxseasha.tban;

import java.util.HashMap;
import ru.foxseasha.tban.Ban;
import ru.foxseasha.tban.utils.BanType;
import ru.foxseasha.tban.utils.MySQL;

public class BanManager {
    public static HashMap<String, Ban> bans = new HashMap();

    public static Ban addBan(String player, BanType type, String owner, long expire, String reason) {
        player = player.toLowerCase();
        Ban b = new Ban(player, type, owner, System.currentTimeMillis(), expire, reason);
        MySQL.execute("INSERT IGNORE INTO `tban` (`player`, `type`, `owner`, `time`, `expire`, `reason`) VALUES ('" + b.getPlayer() + "', '" + (Object)((Object)b.getType()) + "', '" + b.getOwner() + "', '" + b.getTime() + "', '" + b.getExpire() + "', '" + b.getReason() + "')");
        bans.put(player, b);
        return b;
    }

    public static void removeBan(String player) {
        player = player.toLowerCase();
        MySQL.execute("DELETE FROM `tban` WHERE `player`='" + player + "'");
        bans.remove(player);
    }

    public static Ban getBanByPlayer(String player) {
        player = player.toLowerCase();
        return bans.get(player);
    }
}

