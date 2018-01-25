package ru.foxseasha.tban.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import ru.foxseasha.tban.Main;
import ru.foxseasha.tban.utils.Config;
import ru.foxseasha.tban.utils.MySQL;

public class WBCommand
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("TBan.commands.tb")) {
            sender.sendMessage((Object)ChatColor.RED + "\u0423 \u0432\u0430\u0441 \u043d\u0435\u0434\u043e\u0441\u0442\u0430\u0442\u043e\u0447\u043d\u043e \u043f\u0440\u0430\u0432.");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage((Object)ChatColor.YELLOW + "/" + label + " reload - \u041f\u0435\u0440\u0435\u0437\u0430\u0433\u0440\u0443\u0437\u0438\u0442\u044c \u043a\u043e\u043d\u0444\u0438\u0433.");
            return true;
        }
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            Main.config = Config.get("config.yml");
            MySQL.disconnect();
            MySQL.connect();
            sender.sendMessage((Object)ChatColor.GREEN + "\u041a\u043e\u043d\u0444\u0438\u0433 \u0443\u0441\u043f\u0435\u0448\u043d\u043e \u043f\u0435\u0440\u0435\u0437\u0430\u0433\u0440\u0443\u0436\u0435\u043d.");
            return true;
        }
        return true;
    }
}

