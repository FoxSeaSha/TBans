package ru.foxseasha.tban.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.foxseasha.tban.utils.AccessType;
import ru.foxseasha.tban.utils.CheckKeyResult;
import ru.foxseasha.tban.utils.MRep;
import ru.foxseasha.tban.utils.Utils;

public class KickCommand
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("TBan.commands.kick")) {
            sender.sendMessage((Object)ChatColor.RED + "\u0423 \u0432\u0430\u0441 \u043d\u0435\u0434\u043e\u0441\u0442\u0430\u0442\u043e\u0447\u043d\u043e \u043f\u0440\u0430\u0432.");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage((Object)ChatColor.YELLOW + "/" + label + " [\u0438\u0433\u0440\u043e\u043a] [\u043f\u0440\u0438\u0447\u0438\u043d\u0430] - \u041a\u0438\u043a\u043d\u0443\u0442\u044c \u0438\u0433\u0440\u043e\u043a\u0430.");
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
                sender.sendMessage((Object)ChatColor.RED + "\u0412\u044b \u043d\u0435 \u043c\u043e\u0436\u0435\u0442\u0435 \u043a\u0438\u043a\u043d\u0443\u0442\u044c \u0441\u0430\u043c\u0438 \u0441\u0435\u0431\u044f.");
                return true;
            }
            Player pl = Utils.getPlayer(args[0]);
            if (pl == null) {
                sender.sendMessage((Object)ChatColor.RED + "\u0418\u0433\u0440\u043e\u043a \u043d\u0435 \u0432 \u0441\u0435\u0442\u0438.");
                return true;
            }
            if (!Utils.checkAccess(AccessType.KICK, sender, args[0])) {
                sender.sendMessage((Object)ChatColor.RED + "\u0418\u0433\u0440\u043e\u043a \u0438\u043c\u0435\u0435\u0442 \u0437\u0430\u0449\u0438\u0442\u0443 \u043e\u0442 \u043a\u0438\u043a\u0430.");
                return true;
            }
            if (args.length == 1) {
                sender.sendMessage((Object)ChatColor.RED + "\u0423\u043a\u0430\u0436\u0438\u0442\u0435 \u043f\u0440\u0438\u0447\u0438\u043d\u0443.");
                return true;
            }
            String reason = Utils.buildReason(args, 1);
            if (!silent) {
                Bukkit.broadcastMessage((String)Utils.buildMessage("broadcast_kick", new MRep("%player%", args[0]), new MRep("%owner%", sender.getName()), new MRep("%reason%", reason)));
            } else {
                sender.sendMessage(Utils.buildMessage("broadcast_kick", new MRep("%player%", args[0]), new MRep("%owner%", sender.getName()), new MRep("%reason%", reason)));
            }
            pl.kickPlayer(Utils.buildMessage("denymsg_kick", new MRep("%owner%", sender.getName()), new MRep("%reason%", reason)));
            return true;
        }
        return true;
    }
}


