package ru.foxseasha.tban.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import ru.foxseasha.tban.Main;
import ru.foxseasha.tban.utils.AccessType;
import ru.foxseasha.tban.utils.BuildTimeResult;
import ru.foxseasha.tban.utils.CheckKeyResult;
import ru.foxseasha.tban.utils.MRep;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Utils
{
    public static String buildReason(final String[] args, final int start) {
        final StringBuilder r = new StringBuilder();
        for (int i = start; i < args.length; ++i) {
            if (args[i] != null) {
                r.append(String.valueOf(args[i]) + " ");
            }
        }
        return r.toString().substring(0, r.toString().length() - 1);
    }
    
    public static BuildTimeResult buildTime(String time) {
        long multi = 1L;
        String unit = null;
        if (time.endsWith("s")) {
            time = time.replaceAll("s", "");
            unit = "\u0441\u0435\u043a\u0443\u043d\u0434";
        }
        else if (time.endsWith("m")) {
            time = time.replaceAll("m", "");
            multi = 60L;
            unit = "\u043c\u0438\u043d\u0443\u0442";
        }
        else if (time.endsWith("h")) {
            time = time.replaceAll("h", "");
            multi = 3600L;
            unit = "\u0447\u0430\u0441\u043e\u0432";
        }
        else if (time.endsWith("d")) {
            time = time.replaceAll("d", "");
            multi = 86400L;
            unit = "\u0434\u043d\u0435\u0439";
        }
        else if (time.endsWith("mo")) {
            time = time.replaceAll("mo", "");
            multi = 2678400L;
            unit = "\u043c\u0435\u0441\u044f\u0446\u0435\u0432";
        }
        else {
            if (!time.endsWith("y")) {
                return new BuildTimeResult(false);
            }
            time = time.replaceAll("y", "");
            multi = 31536000L;
            unit = "\u043b\u0435\u0442";
        }
        try {
            return new BuildTimeResult(true, Long.parseLong(time) * multi, Long.parseLong(time), unit);
        }
        catch (Exception e) {
            return new BuildTimeResult(false);
        }
    }
    
    public static String buildMessage(String msg, final MRep... args) {
        msg = Main.config.getString("messages." + msg);
        for (final MRep arg : args) {
            msg = msg.replaceAll(arg.getKey(), arg.getValue());
        }
        return msg.replaceAll("%newline%", "\n");
    }
    
    public static boolean checkAccess(final AccessType type, final CommandSender sender, final String target) {
        if (Bukkit.getPluginManager().getPlugin("PermissionsEx") == null) {
            return true;
        }
        if (sender instanceof ConsoleCommandSender) {
            return true;
        }
        final String ty = type.toString().toLowerCase();
        if (Main.config.getStringList("protect.players_protected").contains(target)) {
            return false;
        }
        if (Main.config.getStringList("protect.players_overrided").contains(sender.getName())) {
            return true;
        }
        final PermissionUser t = PermissionsEx.getUser(target);
        final boolean sover = sender.hasPermission("TBan.override." + ty);
        final boolean tbya = t.has("TBan.bypass.a." + ty);
        final boolean tbyb = t.has("TBan.bypass.b." + ty);
        return (!sover || !tbyb) && (sover || (!tbya && !tbyb));
    }
    
    public static Player getPlayer(final String str) {
        for (final Player pl : Bukkit.getOnlinePlayers()) {
            if (pl.getName().equalsIgnoreCase(str)) {
                return pl;
            }
        }
        return null;
    }
    
    public static CheckKeyResult checkKey(String[] args, final String key) {
        String[] array;
        for (int length = (array = args).length, i = 0; i < length; ++i) {
            final String arg = array[i];
            if (arg.equalsIgnoreCase(key)) {
                final List<String> list = new ArrayList<String>(Arrays.asList(args));
                list.removeAll(Arrays.asList(key));
                args = list.toArray(args);
                return new CheckKeyResult(list.toArray(args));
            }
        }
        return new CheckKeyResult();
    }
    
    public static long checkTime(final AccessType type, final CommandSender sender, final long time) {
        if (Bukkit.getPluginManager().getPlugin("PermissionsEx") == null) {
            return -1L;
        }
        if (sender instanceof ConsoleCommandSender) {
            return -1L;
        }
        for (final String str : Main.config.getConfigurationSection("limits." + type.toString().toLowerCase() + ".max_time").getValues(false).keySet()) {
            if (PermissionsEx.getPermissionManager().getUser(sender.getName()).inGroup(str)) {
                return Main.config.getLong("limits." + type.toString().toLowerCase() + ".max_time." + str);
            }
        }
        return -1L;
    }
    
    public static boolean checkIp(final String text) {
        final Pattern p = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
        final Matcher m = p.matcher(text);
        return m.find();
    }
}

