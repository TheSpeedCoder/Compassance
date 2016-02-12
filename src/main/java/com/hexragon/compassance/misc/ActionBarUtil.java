package com.hexragon.compassance.misc;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ActionBarUtil
{
    public static String bukkitPackage = Bukkit.getServer().getClass().getPackage().getName();
    public static String nmsver = bukkitPackage.substring(bukkitPackage.lastIndexOf(".") + 1);

    public static void sendActionBar(Player p, String str)
    {
        try
        {
            Class<?> c1 = Class.forName("org.bukkit.craftbukkit." + nmsver + ".entity.CraftPlayer");
            Object p1 = c1.cast(p);
            Object ppoc;
            Class<?> c4 = Class.forName("net.minecraft.server." + nmsver + ".PacketPlayOutChat");
            Class<?> c5 = Class.forName("net.minecraft.server." + nmsver + ".Packet");
            if (nmsver.equalsIgnoreCase("v1_8_R1") || !nmsver.startsWith("v1_8_"))
            {
                Class<?> c2 = Class.forName("net.minecraft.server." + nmsver + ".ChatSerializer");
                Class<?> c3 = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
                Method m3 = c2.getDeclaredMethod("a", String.class);
                Object cbc = c3.cast(m3.invoke(c2, "{\"text\": \"" + str + "\"}"));
                ppoc = c4.getConstructor(new Class<?>[]{c3, byte.class}).newInstance(cbc, (byte) 2);
            }
            else
            {
                Class<?> c2 = Class.forName("net.minecraft.server." + nmsver + ".ChatComponentText");
                Class<?> c3 = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
                Object o = c2.getConstructor(new Class<?>[]{String.class}).newInstance(str);
                ppoc = c4.getConstructor(new Class<?>[]{c3, byte.class}).newInstance(o, (byte) 2);
            }
            Method m1 = c1.getDeclaredMethod("getHandle");
            Object h = m1.invoke(p1);
            Field f1 = h.getClass().getDeclaredField("playerConnection");
            Object pc = f1.get(h);
            Method m5 = pc.getClass().getDeclaredMethod("sendPacket", c5);
            m5.invoke(pc, ppoc);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

//    public static void sendActionBar2(Player p, String str)
//    {
//        IChatBaseComponent c = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + str.replace("\"", "") + "\"}");
//        PacketPlayOutChat packet = new PacketPlayOutChat(c, (byte) 2);
//        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
//    }
}