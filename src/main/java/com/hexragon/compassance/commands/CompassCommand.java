package com.hexragon.compassance.commands;

import com.hexragon.compassance.Compassance;
import com.hexragon.compassance.managers.files.configs.PlayerConfig;
import com.hexragon.compassance.misc.Misc;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CompassCommand implements CommandExecutor
{
    public CompassCommand()
    {
        Compassance.instance.getCommand("compass").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage("You must be a player to execute this command.");
            return true;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("open"))
        {
            Compassance.mainMenu.show((Player) sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("theme"))
        {
            Player p = (Player) sender;

            if (args.length < 2)
            {
                p.sendMessage(Misc.fmtClr("&a&lCOMPASS &8» &cInsufficient amount of arguments."));
                p.sendMessage(Misc.fmtClr("&a&lUSAGE &8» &7/compass theme &ftheme-id"));
                return true;
            }

            if (Compassance.themeManager.getTheme(args[1]) == null)
            {
                p.sendMessage(Misc.fmtClr("&a&lCOMPASS &8» &cTheme ID doesn't exist."));
                return true;
            }

            p.sendMessage(Misc.fmtClr(String.format("&a&lCOMPASS &8» &7Switching your selected theme to &r%s&7.", Compassance.themeManager.getTheme(args[1]).getName())));
            Compassance.playerConfig.config.set(String.format(PlayerConfig.SETTING_SELECTEDTHEME, p.getPlayer().getUniqueId().toString()), args[1]);
            return true;
        }

        if (args[0].equalsIgnoreCase("track") || args[0].equalsIgnoreCase("trk"))
        {
            Player p = (Player) sender;

            boolean b = Compassance.playerConfig.config.getBoolean(String.format(PlayerConfig.SETTING_TRACKING, p.getPlayer().getUniqueId().toString()));
            if (!b)
            {
                p.sendMessage(Misc.fmtClr("&a&lCOMPASS &8» &cYou must enable tracking in the Compassance menu."));
                return true;
            }

            if (args.length < 2)
            {
                p.sendMessage(Misc.fmtClr("&a&lCOMPASS &8» &cInsufficient amount of arguments."));
                p.sendMessage(Misc.fmtClr("&a&lUSAGE &8» &7/compass trk &fpl &7or &7/compass trk &floc"));
                return true;
            }

            if (args[1].equalsIgnoreCase("player") || args[1].equalsIgnoreCase("pl"))
            {
                if (args.length != 3)
                {
                    p.sendMessage(Misc.fmtClr("&a&lCOMPASS &8» &cInsufficient amount of arguments."));
                    p.sendMessage(Misc.fmtClr("&a&lUSAGE &8» &7/compass trk pl &f-player"));
                    return true;
                }

                String targetName = args[2];
                for (Player pl : Bukkit.getOnlinePlayers())
                {
                    if (pl.getName().equalsIgnoreCase(targetName))
                    {
                        Compassance.trackingManager.newTracking(p, pl);
                        Compassance.compassTaskManager.refresh(p);
                        p.sendMessage(Misc.fmtClr(String.format("&a&lCOMPASS &8» &7You are now tracking player &f%s&7.", pl.getName())));
                        pl.sendMessage(Misc.fmtClr(String.format("&a&lCOMPASS &8» &7You are being tracked by &f%s&7.", p.getName())));
                        return true;
                    }
                }
                p.sendMessage(Misc.fmtClr("&a&lCOMPASS &8» &cThe player you are attempting to track is not found."));

                return true;
            }
            else if (args[1].equalsIgnoreCase("location") || args[1].equalsIgnoreCase("loc"))
            {
                if (args.length != 5)
                {
                    p.sendMessage(Misc.fmtClr("&a&lCOMPASS &8» &cInsufficient amount of arguments."));
                    p.sendMessage(Misc.fmtClr("&a&lUSAGE &8» &7/compass trk loc &f-x -y -z&7."));
                    return true;
                }

                try
                {
                    Double x = Double.parseDouble(args[2]);
                    Double y = Double.parseDouble(args[3]);
                    Double z = Double.parseDouble(args[4]);
                    Compassance.trackingManager.newTracking(p, new Location(p.getWorld(), x, y, z));

                    Compassance.compassTaskManager.refresh(p);
                    p.sendMessage(Misc.fmtClr(String.format("&a&lCOMPASS &8» &7You are now tracking coordinates &f%s&7, &f%s&7, &f%s&7.", x, y, z)));
                    return true;
                }
                catch (Exception e)
                {
                    p.sendMessage(Misc.fmtClr("&a&lCOMPASS &8» &cThe coordinates you entered failed to parse, make sure you are using numbers."));
                }
            }
            else
            {
                p.sendMessage(Misc.fmtClr("&a&lCOMPASS &8» &cInvalid tracking type."));
                p.sendMessage(Misc.fmtClr("&a&lUSAGE &8» &7/compass trk &fpl &7or &7/compass trk &floc"));
                return true;
            }

        }

        return false;
    }
}