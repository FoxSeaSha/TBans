package ru.foxseasha.tban.commands;

import java.net.InetSocketAddress;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.foxseasha.tban.Ban;
import ru.foxseasha.tban.BanManager;
import ru.foxseasha.tban.utils.AccessType;
import ru.foxseasha.tban.utils.BanType;
import ru.foxseasha.tban.utils.CheckKeyResult;
import ru.foxseasha.tban.utils.MRep;
import ru.foxseasha.tban.utils.Utils;

public class BanIpCommand
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("TBan.commands.banip")) {
            sender.sendMessage((Object)ChatColor.RED + "\u0423 \u0432\u0430\u0441 \u043d\u0435\u0434\u043e\u0441\u0442\u0430\u0442\u043e\u0447\u043d\u043e \u043f\u0440\u0430\u0432.");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage((Object)ChatColor.YELLOW + "/" + label + " [\u0438\u0433\u0440\u043e\u043a] [\u043f\u0440\u0438\u0447\u0438\u043d\u0430] - \u0417\u0430\u0431\u0430\u043d\u0438\u0442\u044c \u0438\u0433\u0440\u043e\u043a\u0430 \u043f\u043e IP \u043d\u0430\u0432\u0441\u0435\u0433\u0434\u0430.");
            sender.sendMessage((Object)ChatColor.YELLOW + "/" + label + " [ip] [\u043f\u0440\u0438\u0447\u0438\u043d\u0430] - \u0417\u0430\u0431\u0430\u043d\u0438\u0442\u044c IP \u043d\u0430\u0432\u0441\u0435\u0433\u0434\u0430.");
            return true;
        }
        if (args.length > 0) {
            String ip;
            boolean silent = false;
            CheckKeyResult s = Utils.checkKey(args, "-s");
            if (s.getResult()) {
                args = s.getArguments();
                silent = true;
            }
            if (sender instanceof Player && (args[0].equalsIgnoreCase(ip = ((Player)sender).getAddress().getHostString()) || args[0].equalsIgnoreCase(sender.getName()))) {
                sender.sendMessage((Object)ChatColor.RED + "\u0412\u044b \u043d\u0435 \u043c\u043e\u0436\u0435\u0442\u0435 \u0437\u0430\u0431\u0430\u043d\u0438\u0442\u044c \u0441\u0430\u043c\u0438 \u0441\u0435\u0431\u044f.");
                return true;
            }
            for (Player pl : Bukkit.getOnlinePlayers()) {
                if (!args[0].equalsIgnoreCase(pl.getName()) && !args[0].equals(pl.getAddress().getHostString()) || Utils.checkAccess(AccessType.BANIP, sender, pl.getName())) continue;
                sender.sendMessage((Object)ChatColor.RED + "\u0418\u0433\u0440\u043e\u043a \u0438\u043c\u0435\u0435\u0442 \u0437\u0430\u0449\u0438\u0442\u0443 \u043e\u0442 \u0431\u0430\u043d\u0430.");
                return true;
            }
            ip = args[0];
            Player pl = Utils.getPlayer(ip);
            if (pl != null) {
                ip = Utils.getPlayer(ip).getAddress().getHostString();
            } else if (!Utils.checkIp(ip)) {
                sender.sendMessage((Object)ChatColor.RED + "\u0418\u0433\u0440\u043e\u043a \u043d\u0435 \u0432 \u0441\u0435\u0442\u0438 \u0438\u043b\u0438 \u043d\u0435\u043f\u0440\u0430\u0432\u0438\u043b\u044c\u043d\u043e \u0443\u043a\u0430\u0437\u0430\u043d IP \u0430\u0434\u0440\u0435\u0441.");
                return true;
            }
            if (args.length == 1) {
                sender.sendMessage((Object)ChatColor.RED + "\u0423\u043a\u0430\u0436\u0438\u0442\u0435 \u043f\u0440\u0438\u0447\u0438\u043d\u0443.");
                return true;
            }
            Ban b = BanManager.getBanByPlayer(args[0]);
            if (BanManager.getBanByPlayer(args[0]) != null) {
                sender.sendMessage((Object)ChatColor.RED + "\u0418\u0433\u0440\u043e\u043a \u0443\u0436\u0435 " + (b.getType() == BanType.BAN ? "\u0437\u0430\u0431\u0430\u043d\u0435\u043d" : (b.getType() == BanType.MUTE ? "\u0437\u0430\u043c\u0443\u0447\u0435\u043d" : "\u0437\u0430\u0431\u0430\u043d\u0435\u043d \u043f\u043e IP")) + ".");
                return true;
            }
            String reason = Utils.buildReason(args, 1);
            BanManager.addBan(ip, BanType.BANIP, sender.getName(), 0, reason);
            if (!silent) {
                Bukkit.broadcastMessage((String)Utils.buildMessage("broadcast_banip", new MRep("%player%", ip), new MRep("%owner%", sender.getName()), new MRep("%reason%", reason)));
            } else {
                sender.sendMessage(Utils.buildMessage("broadcast_banip", new MRep("%player%", ip), new MRep("%owner%", sender.getName()), new MRep("%reason%", reason)));
            }
            if (pl != null) {
                pl.kickPlayer(Utils.buildMessage("denymsg_banip", new MRep("%owner%", sender.getName()), new MRep("%reason%", reason)));
            }
            return true;
        }
        return true;
    }
}


