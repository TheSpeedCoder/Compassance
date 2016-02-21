package com.hexragon.compassance;

import com.hexragon.compassance.commands.CompassCommand;
import com.hexragon.compassance.commands.ReloadCommand;
import com.hexragon.compassance.commands.TestCommand;
import com.hexragon.compassance.files.Gearbox;
import com.hexragon.compassance.files.configs.MainConfig;
import com.hexragon.compassance.files.text.GearboxText;
import com.hexragon.compassance.gui.MainMenu;
import com.hexragon.compassance.gui.SettingsMenu;
import com.hexragon.compassance.gui.ThemeMenu;
import com.hexragon.compassance.managers.compass.tasks.CTaskManager;
import com.hexragon.compassance.managers.compass.tasks.tracking.TrackingManager;
import com.hexragon.compassance.managers.themes.ThemeManager;
import com.hexragon.compassance.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.MetricsLite;

import java.io.IOException;

public class Main extends JavaPlugin
{
    public static Main instance;

    public static Gearbox themeConfig;
    public static Gearbox playerConfig;
    public static Gearbox mainConfig;

    public static CTaskManager taskManager;
    public static ThemeManager themeManager;
    public static TrackingManager trackingManager;

    public static MainMenu mainMenu;
    public static SettingsMenu settingsMenu;
    public static ThemeMenu themeMenu;

    public static boolean placeholderAPIExist;

    public void onEnable()
    {
        instance = this;

        // LOAD CONFIGURATIONS
        mainConfig = new Gearbox(this, "config.yml");
        Utils.versionCheck(mainConfig);
        mainConfig.load();

        themeConfig = new Gearbox(this, "themes-config.yml");
        Utils.versionCheck(themeConfig);
        themeConfig.load();

        playerConfig = new Gearbox(this, "players-config.yml");
        Utils.versionCheck(playerConfig);
        playerConfig.load();

        // MANAGERS INSTANTIATION
        themeManager = new ThemeManager();
        taskManager = new CTaskManager();
        trackingManager = new TrackingManager();

        mainMenu = new MainMenu();
        settingsMenu = new SettingsMenu();
        themeMenu = new ThemeMenu();

        themeManager.loadThemes();
        taskManager.newTaskAll();

        // SECONDARY INSTANTIATION
        new GearboxText(this, "references.txt").load();
        new CompassCommand();
        new ReloadCommand();

        if (mainConfig.config.getBoolean(MainConfig.DEBUG_MODE.path))
        {
            new TestCommand();

            if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null && Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
            {
                placeholderAPIExist = true;
                getLogger().info("Detected PlaceholderAPI.");
                new Placeholders(this);
            }
            else
            {
                placeholderAPIExist = false;
                getLogger().info("Did not find PlaceholderAPI.");
            }
        }

        // METRICS
        if (mainConfig.config.getBoolean(MainConfig.METRICS.path))
        {
            try
            {
                MetricsLite metrics = new MetricsLite(this);
                metrics.start();
                getLogger().info("Metrics initialized. You can disable this by going into the main config.");
            }
            catch (IOException e)
            {
                getLogger().warning("Unable to submit data to Metrics.");
            }
        }
        else
        {
            getLogger().info("Metrics is disabled.");
        }

        new Listeners();
    }

    double damn()
    {
        return 2;
    }


    public void onDisable()
    {
        playerConfig.save();
        taskManager.endTaskAll();
        instance = null;
    }
}
