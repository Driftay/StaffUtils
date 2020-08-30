package me.mikro.staffutils.utils;

import me.mikro.staffutils.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FolderYamlFile {

    private File file;
    private FileConfiguration config;

    public FolderYamlFile(String name, Main instance) {
        file = new File(instance.getDataFolder(), name + ".yml");

        if (!file.exists()) {
            instance.getDataFolder().mkdirs();

            if (name.contains("/")) {
                file.getParentFile().mkdirs();
            }

            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
