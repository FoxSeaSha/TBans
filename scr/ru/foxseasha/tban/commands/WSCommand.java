package ru.foxseasha.tban.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import ru.foxseasha.tban.Ban;
import ru.foxseasha.tban.BanManager;
import ru.foxseasha.tban.utils.BanType;

public class WSCommand
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("TBan.commands.ts")) {
            sender.sendMessage((Object)ChatColor.RED + "\u0423 \u0432\u0430\u0441 \u043d\u0435\u0434\u043e\u0441\u0442\u0430\u0442\u043e\u0447\u043d\u043e \u043f\u0440\u0430\u0432.");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage((Object)ChatColor.YELLOW + "/" + label + " [ip] - \u0423\u0437\u043d\u0430\u0442\u044c \u043e \u0431\u0430\u043d\u0435 IP \u0430\u0434\u0440\u0435\u0441\u0430.");
            sender.sendMessage((Object)ChatColor.YELLOW + "/" + label + " [\u0438\u0433\u0440\u043e\u043a] - \u0423\u0437\u043d\u0430\u0442\u044c \u043e \u0431\u0430\u043d\u0435 \u0438\u0433\u0440\u043e\u043a\u0430.");
            return true;
        }
        if (args.length > 0) {
            Ban ban = BanManager.getBanByPlayer(args[0]);
            if (ban == null) {
                sender.sendMessage((Object)ChatColor.RED + "\u0411\u0430\u043d \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d.");
                return true;
            }
            sender.sendMessage((Object)ChatColor.GREEN + "\u0418\u0433\u0440\u043e\u043a: " + (Object)ChatColor.YELLOW + ban.getPlayer());
            sender.sendMessage((Object)ChatColor.GREEN + "\u0417\u0430\u0431\u0430\u043d\u0435\u043d \u0430\u0434\u043c\u0438\u043d\u043e\u043c: " + (Object)ChatColor.YELLOW + ban.getOwner());
            sender.sendMessage((Object)ChatColor.GREEN + "\u0422\u0438\u043f \u0431\u0430\u043d\u0430: " + (Object)ChatColor.YELLOW + (Object)((Object)ban.getType()));
            sender.sendMessage((Object)ChatColor.GREEN + "\u041f\u0440\u0438\u0447\u0438\u043d\u0430: " + (Object)ChatColor.YELLOW + ban.getReason());
            sender.sendMessage((Object)ChatColor.GREEN + "\u0420\u0430\u0437\u0431\u0430\u043d \u0447\u0435\u0440\u0435\u0437: " + (Object)ChatColor.YELLOW + (ban.getExpire() == 0 ? "\u043d\u0438\u043a\u043e\u0433\u0434\u0430" : new StringBuilder().append(ban.getTimeLeft()).toString()));
        }
        return true;
    }
}
