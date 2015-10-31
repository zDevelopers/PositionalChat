package fr.zcraft.PositionalChat;

import org.bukkit.plugin.java.JavaPlugin;


public class PositionalChat extends JavaPlugin
{
    private static PositionalChat instance;

    private int CLEAR_BEFORE;
    private int OBFUSCATED_AFTER;


    @Override
    public void onEnable()
    {
        instance = this;

        saveDefaultConfig();

        CLEAR_BEFORE     = getConfig().getInt("distances.clearBefore", 30);
        OBFUSCATED_AFTER = getConfig().getInt("distances.obfuscatedAfter", 150);

        getServer().getPluginManager().registerEvents(new ChatListener(), this);
    }


    public int getClearBefore()
    {
        return CLEAR_BEFORE;
    }

    public int getObfuscatedAfter()
    {
        return OBFUSCATED_AFTER;
    }


    public static PositionalChat get()
    {
        return instance;
    }
}
