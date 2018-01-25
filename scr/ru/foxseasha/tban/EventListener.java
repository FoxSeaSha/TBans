package ru.foxseasha.tban;

import java.net.InetAddress;
import java.util.List;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import ru.foxseasha.tban.Ban;
import ru.foxseasha.tban.BanManager;
import ru.foxseasha.tban.Main;
import ru.foxseasha.tban.utils.BanType;
import ru.foxseasha.tban.utils.MRep;
import ru.foxseasha.tban.utils.Utils;

public class EventListener
implements Listener {
    @EventHandler
    public void PlayerLoginEvent(PlayerLoginEvent event) {
        Ban ban = BanManager.getBanByPlayer(event.getPlayer().getName());
        if (ban != null && ban.getType() == BanType.BAN) {
            if (ban.getExpire() == 0) {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Utils.buildMessage("denymsg_ban", new MRep("%owner%", ban.getOwner()), new MRep("%reason%", ban.getReason())));
            } else {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Utils.buildMessage("denymsg_tempban", new MRep("%owner%", ban.getOwner()), new MRep("%reason%", ban.getReason()), new MRep("%time%", ban.getTimeLeft())));
            }
            return;
        }
        Ban banip = BanManager.getBanByPlayer(event.getAddress().getHostAddress());
        if (banip != null && banip.getType() == BanType.BANIP) {
            if (banip.getExpire() == 0) {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Utils.buildMessage("denymsg_banip", new MRep("%owner%", banip.getOwner()), new MRep("%reason%", banip.getReason())));
            } else {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Utils.buildMessage("denymsg_tempbanip", new MRep("%owner%", banip.getOwner()), new MRep("%reason%", banip.getReason()), new MRep("%time%", banip.getTimeLeft())));
            }
            return;
        }
    }

    @EventHandler
    public void AsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Ban mute = BanManager.getBanByPlayer(event.getPlayer().getName());
        if (mute != null && mute.getType() == BanType.MUTE) {
            if (mute.getExpire() == 0) {
                event.getPlayer().sendMessage(Utils.buildMessage("denymsg_mute", new MRep("%owner%", mute.getOwner()), new MRep("%reason%", mute.getReason())));
            } else {
                event.getPlayer().sendMessage(Utils.buildMessage("denymsg_tempmute", new MRep("%owner%", mute.getOwner()), new MRep("%reason%", mute.getReason()), new MRep("%time%", mute.getTimeLeft())));
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void PlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        Ban mute = BanManager.getBanByPlayer(event.getPlayer().getName());
        if (mute != null && mute.getType() == BanType.MUTE) {
            String message = event.getMessage();
            for (String str : Main.config.getStringList("limits.mute.blocked_commands")) {
                if (!message.toLowerCase().startsWith(str.toLowerCase()) && !message.equalsIgnoreCase(str)) continue;
                if (mute.getExpire() == 0) {
                    event.getPlayer().sendMessage(Utils.buildMessage("denymsg_mute", new MRep("%owner%", mute.getOwner()), new MRep("%reason%", mute.getReason())));
                } else {
                    event.getPlayer().sendMessage(Utils.buildMessage("denymsg_tempmute", new MRep("%owner%", mute.getOwner()), new MRep("%reason%", mute.getReason()), new MRep("%time%", mute.getTimeLeft())));
                }
                event.setCancelled(true);
                return;
            }
        }
    }
}

