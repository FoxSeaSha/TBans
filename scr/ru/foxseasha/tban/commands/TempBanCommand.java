package ru.foxseasha.tban.commands;

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
import ru.foxseasha.tban.utils.BuildTimeResult;
import ru.foxseasha.tban.utils.CheckKeyResult;
import ru.foxseasha.tban.utils.MRep;
import ru.foxseasha.tban.utils.Utils;

public class TempBanCommand
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("TBan.commands.tempban")) {
            sender.sendMessage((Object)ChatColor.RED + "\u0423 \u0432\u0430\u0441 \u043d\u0435\u0434\u043e\u0441\u0442\u0430\u0442\u043e\u0447\u043d\u043e \u043f\u0440\u0430\u0432.");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage((Object)ChatColor.YELLOW + "/" + label + " [\u0438\u0433\u0440\u043e\u043a] [\u0432\u0440\u0435\u043c\u044f] [\u043f\u0440\u0438\u0447\u0438\u043d\u0430] - \u0417\u0430\u0431\u0430\u043d\u0438\u0442\u044c \u0438\u0433\u0440\u043e\u043a\u0430 \u043d\u0430 \u0432\u0440\u0435\u043c\u044f.");
            sender.sendMessage((Object)ChatColor.GRAY + "\u0424\u043e\u0440\u043c\u0430\u0442 \u0443\u043a\u0430\u0437\u0430\u043d\u0438\u044f \u0432\u0440\u0435\u043c\u0435\u043d\u0438:");
            sender.sendMessage((Object)ChatColor.GRAY + " 10s - 10 \u0441\u0435\u043a\u0443\u043d\u0434");
            sender.sendMessage((Object)ChatColor.GRAY + " 10m - 10 \u043c\u0438\u043d\u0443\u0442");
            sender.sendMessage((Object)ChatColor.GRAY + " 10h - 10 \u0447\u0430\u0441\u043e\u0432");
            sender.sendMessage((Object)ChatColor.GRAY + " 10d - 10 \u0434\u043d\u0435\u0439");
            sender.sendMessage((Object)ChatColor.GRAY + " 10mo - 10 \u043c\u0435\u0441\u044f\u0446\u0435\u0432");
            sender.sendMessage((Object)ChatColor.GRAY + " 10y - 10 \u043b\u0435\u0442");
            return true;
        }
        if (args.length > 0) {
            boolean silent = false;
            CheckKeyResult s = Utils.checkKey(args, "-s");
            if (s.getResult()) {
                args = s.getArguments();
                silent = true;
            }
            if (sender.getName().equalsIgnoreCase(args[0])) {
                sender.sendMessage((Object)ChatColor.RED + "\u0412\u044b \u043d\u0435 \u043c\u043e\u0436\u0435\u0442\u0435 \u0437\u0430\u0431\u0430\u043d\u0438\u0442\u044c \u0441\u0430\u043c\u0438 \u0441\u0435\u0431\u044f.");
                return true;
            }
            if (Utils.getPlayer(args[0]) == null && !sender.hasPermission("TBan.offline")) {
                sender.sendMessage((Object)ChatColor.RED + "\u0418\u0433\u0440\u043e\u043a \u0434\u043e\u043b\u0436\u0435\u043d \u0431\u044b\u0442\u044c \u0432 \u0441\u0435\u0442\u0438.");
                return true;
            }
            if (!Utils.checkAccess(AccessType.TEMPBAN, sender, args[0])) {
                sender.sendMessage((Object)ChatColor.RED + "\u0418\u0433\u0440\u043e\u043a \u0438\u043c\u0435\u0435\u0442 \u0437\u0430\u0449\u0438\u0442\u0443 \u043e\u0442 \u0431\u0430\u043d\u0430.");
                return true;
            }
            if (args.length == 1) {
                sender.sendMessage((Object)ChatColor.RED + "\u0423\u043a\u0430\u0436\u0438\u0442\u0435 \u0432\u0440\u0435\u043c\u044f.");
                return true;
            }
            BuildTimeResult t = Utils.buildTime(args[1]);
            if (!t.getResult()) {
                sender.sendMessage((Object)ChatColor.RED + "\u0412\u0440\u0435\u043c\u044f \u0443\u043a\u0430\u0437\u0430\u043d\u043e \u043d\u0435 \u043f\u0440\u0430\u0432\u0438\u043b\u044c\u043d\u043e.");
                return true;
            }
            long time = t.getTime();
            if (time <= 0) {
                sender.sendMessage((Object)ChatColor.RED + "\u0412\u0440\u0435\u043c\u044f \u0434\u043e\u043b\u0436\u043d\u043e \u0431\u044b\u0442\u044c \u043f\u043e\u043b\u043e\u0436\u0438\u0442\u0435\u043b\u044c\u043d\u044b\u043c.");
                return true;
            }
            long ct = Utils.checkTime(AccessType.BAN, sender, time);
            if (ct != -1 && ct * 60 < time) {
                sender.sendMessage((Object)ChatColor.RED + "\u041f\u0440\u0435\u0432\u044b\u0448\u0435\u043d\u043e \u043c\u0430\u043a\u0441\u0438\u043c\u0430\u043b\u044c\u043d\u043e \u0434\u043e\u043f\u0443\u0441\u0442\u0438\u043c\u043e\u0435 \u0432\u0440\u0435\u043c\u044f. (\u043c\u0430\u043a\u0441\u0438\u043c\u0443\u043c: " + ct + " \u043c\u0438\u043d\u0443\u0442)");
                return true;
            }
            if (args.length == 2) {
                sender.sendMessage((Object)ChatColor.RED + "\u0423\u043a\u0430\u0436\u0438\u0442\u0435 \u043f\u0440\u0438\u0447\u0438\u043d\u0443.");
                return true;
            }
            Ban b = BanManager.getBanByPlayer(args[0]);
            if (BanManager.getBanByPlayer(args[0]) != null) {
                sender.sendMessage((Object)ChatColor.RED + "\u0418\u0433\u0440\u043e\u043a \u0443\u0436\u0435 " + (b.getType() == BanType.BAN ? "\u0437\u0430\u0431\u0430\u043d\u0435\u043d" : (b.getType() == BanType.MUTE ? "\u0437\u0430\u043c\u0443\u0447\u0435\u043d" : "\u0437\u0430\u0431\u0430\u043d\u0435\u043d \u043f\u043e IP")) + ".");
                return true;
            }
            String reason = Utils.buildReason(args, 2);
            BanManager.addBan(args[0], BanType.BAN, sender.getName(), System.currentTimeMillis() + time * 1000, reason);
            if (!silent) {
                Bukkit.broadcastMessage((String)Utils.buildMessage("broadcast_tempban", new MRep("%player%", args[0]), new MRep("%owner%", sender.getName()), new MRep("%reason%", reason), new MRep("%time%", String.valueOf(t.getRaw()) + " " + t.getUnit())));
            } else {
                sender.sendMessage(Utils.buildMessage("broadcast_tempban", new MRep("%player%", args[0]), new MRep("%owner%", sender.getName()), new MRep("%reason%", reason), new MRep("%time%", String.valueOf(t.getRaw()) + " " + t.getUnit())));
            }
            Player pl = Utils.getPlayer(args[0]);
            if (pl != null) {
                pl.kickPlayer(Utils.buildMessage("denymsg_tempban", new MRep("%owner%", sender.getName()), new MRep("%reason%", reason), new MRep("%time%", String.valueOf(t.getRaw()) + " " + t.getUnit())));
            }
            return true;
        }
        return true;
    }
}