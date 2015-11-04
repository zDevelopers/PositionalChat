package fr.zcraft.PositionalChat;

import org.bukkit.plugin.java.JavaPlugin;


public class PositionalChat extends JavaPlugin
{
    private static PositionalChat instance;

    private PCConfig config;
    private TextObfuscator textObfuscator;


    @Override
    public void onEnable()
    {
        instance = this;

        saveDefaultConfig();

        config = new PCConfig();
        textObfuscator = new TextObfuscator();

        getServer().getPluginManager().registerEvents(new ChatListener(), this);
    }


    public PCConfig getPCConfig()
    {
        return config;
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
