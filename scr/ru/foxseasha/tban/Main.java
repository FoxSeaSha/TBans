package ru.foxseasha.tban;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import ru.foxseasha.tban.EventListener;
import ru.foxseasha.tban.commands.BanCommand;
import ru.foxseasha.tban.commands.BanIpCommand;
import ru.foxseasha.tban.commands.KickCommand;
import ru.foxseasha.tban.commands.MuteCommand;
import ru.foxseasha.tban.commands.TempBanCommand;
import ru.foxseasha.tban.commands.TempBanIpCommand;
import ru.foxseasha.tban.commands.TempMuteCommand;
import ru.foxseasha.tban.commands.UnbanCommand;
import ru.foxseasha.tban.commands.UnbanIpCommand;
import ru.foxseasha.tban.commands.UnmuteCommand;
import ru.foxseasha.tban.commands.WBCommand;
import ru.foxseasha.tban.commands.WSCommand;
import ru.foxseasha.tban.utils.Config;
import ru.foxseasha.tban.utils.Logger;
import ru.foxseasha.tban.utils.MySQL;

public class Main
extends JavaPlugin {
    public static FileConfiguration config;
    public static Plugin plugin;

    public void onEnable() {
        long time = System.currentTimeMillis();
        plugin = this;
        config = Config.get("config.yml");
        MySQL.getBans();
        MySQL.getBansAsync();
        this.getCommand("ts").setExecutor((CommandExecutor)new WSCommand());
        this.getCommand("tb").setExecutor((CommandExecutor)new WBCommand());
        this.getCommand("kick").setExecutor((CommandExecutor)new KickCommand());
        this.getCommand("ban").setExecutor((CommandExecutor)new BanCommand());
        this.getCommand("tempban").setExecutor((CommandExecutor)new TempBanCommand());
        this.getCommand("unban").setExecutor((CommandExecutor)new UnbanCommand());
        this.getCommand("banip").setExecutor((CommandExecutor)new BanIpCommand());
        this.getCommand("tempbanip").setExecutor((CommandExecutor)new TempBanIpCommand());
        this.getCommand("unbanip").setExecutor((CommandExecutor)new UnbanIpCommand());
        this.getCommand("mute").setExecutor((CommandExecutor)new MuteCommand());
        this.getCommand("tempmute").setExecutor((CommandExecutor)new TempMuteCommand());
        this.getCommand("unmute").setExecutor((CommandExecutor)new UnmuteCommand());
        Bukkit.getPluginManager().registerEvents((Listener)new EventListener(), (Plugin)this);
        Logger.info("\u041f\u043b\u0430\u0433\u0438\u043d \u0432\u043a\u043b\u044e\u0447\u0435\u043d. (" + (Object)ChatColor.YELLOW + (System.currentTimeMillis() - time) + " ms" + (Object)ChatColor.GREEN + ")");
    }

    public void onDisable() {
        Logger.info("\u041f\u043b\u0430\u0433\u0438\u043d \u0432\u044b\u043a\u043b\u044e\u0447\u0435\u043d.");
    }
}

