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

        CLEAR_BEFORE     = Math.pow(getConfig().getDouble("distances.clearBefore", 30), 2);
        OBFUSCATED_AFTER = Math.pow(getConfig().getDouble("distances.obfuscatedAfter", 150), 2);

        getServer().getPluginManager().registerEvents(new ChatListener(), this);

        textObfuscator = new TextObfuscator();
    }


    public double getClearBeforeSquared()
    {
        return CLEAR_BEFORE;
    }

    public double getObfuscatedAfterSquared()
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
