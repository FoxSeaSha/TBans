package ru.foxseasha.tban.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import ru.foxseasha.tban.Ban;
import ru.foxseasha.tban.BanManager;
import ru.foxseasha.tban.utils.BanType;
import ru.foxseasha.tban.utils.CheckKeyResult;
import ru.foxseasha.tban.utils.MRep;
import ru.foxseasha.tban.utils.Utils;

public class UnmuteCommand
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("TBan.commands.unmute")) {
            sender.sendMessage((Object)ChatColor.RED + "\u0423 \u0432\u0430\u0441 \u043d\u0435\u0434\u043e\u0441\u0442\u0430\u0442\u043e\u0447\u043d\u043e \u043f\u0440\u0430\u0432.");
            return true;
        }
        boolean silent = false;
        CheckKeyResult s = Utils.checkKey(args, "-s");
        if (s.getResult()) {
            args = s.getArguments();
            silent = true;
        }
        if (args.length == 0) {
            sender.sendMessage((Object)ChatColor.YELLOW + "/" + label + " [\u0438\u0433\u0440\u043e\u043a] - \u0421\u043d\u044f\u0442\u044c \u043c\u0443\u0442 \u0441 \u0438\u0433\u0440\u043e\u043a\u0430.");
            return true;
        }
        if (args.length > 0) {
            Ban ban = BanManager.getBanByPlayer(args[0]);
            if (ban == null || ban != null && ban.getType() != BanType.MUTE) {
                sender.sendMessage((Object)ChatColor.RED + "\u041d\u0430 \u0438\u0433\u0440\u043e\u043a\u0430 \u043d\u0435 \u043d\u0430\u043b\u043e\u0436\u0435\u043d \u043c\u0443\u0442.");
                return true;
            }
            BanManager.removeBan(ban.getPlayer());
            if (!silent) {
                Bukkit.broadcastMessage((String)Utils.buildMessage("broadcast_unmute", new MRep("%player%", args[0]), new MRep("%owner%", sender.getName())));
            } else {
                sender.sendMessage(Utils.buildMessage("broadcast_unmute", new MRep("%player%", args[0]), new MRep("%owner%", sender.getName())));
            }
            return true;
        }
        return true;
    }
}
