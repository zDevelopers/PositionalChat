package fr.zcraft.PositionalChat;

import org.bukkit.plugin.java.JavaPlugin;


public class PositionalChat extends JavaPlugin
{
    private static PositionalChat instance;

    private TextObfuscator textObfuscator;

    private double CLEAR_BEFORE;
    private double OBFUSCATED_AFTER;


    @Override
    public void onEnable()
    {
        instance = this;

        saveDefaultConfig();

        CLEAR_BEFORE     = getConfig().getDouble("distances.clearBefore", 30);
        OBFUSCATED_AFTER = getConfig().getDouble("distances.obfuscatedAfter", 150);

        getServer().getPluginManager().registerEvents(new ChatListener(), this);

        textObfuscator = new TextObfuscator();
    }


    public double getClearBefore()
    {
        return CLEAR_BEFORE;
    }

    public double getObfuscatedAfter()
    {
        return OBFUSCATED_AFTER;
    }


    public TextObfuscator getTextObfuscator()
    {
        return textObfuscator;
    }

    public static PositionalChat get()
    {
        return instance;
    }
}
