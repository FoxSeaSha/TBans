package ru.foxseasha.tban.utils;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.foxseasha.tban.Main;
import ru.foxseasha.tban.utils.Logger;

public class Config {

   public static FileConfiguration get(String name) {
      File f = new File(Main.plugin.getDataFolder(), name);
      if(Main.plugin.getResource(name) == null) {
         return save(YamlConfiguration.loadConfiguration(f), name);
      } else {
         if(!f.exists()) {
            Main.plugin.saveResource(name, false);
         }

         return YamlConfiguration.loadConfiguration(f);
      }
   }

   public static FileConfiguration save(FileConfiguration config, String name) {
      try {
         config.save(new File(Main.plugin.getDataFolder(), name));
      } catch (IOException var3) {
         Logger.error(var3.getMessage());
      }

      return config;
   }
}
