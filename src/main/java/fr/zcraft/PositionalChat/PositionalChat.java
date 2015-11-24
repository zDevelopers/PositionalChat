package fr.zcraft.PositionalChat;

import fr.zcraft.PositionalChat.commands.PCYellCommand;
import fr.zcraft.PositionalChat.listeners.ChatListener;
import fr.zcraft.PositionalChat.tools.TextObfuscator;
import fr.zcraft.zlib.core.ZPlugin;


public class PositionalChat extends ZPlugin
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

        getCommand("yell").setExecutor(new PCYellCommand());
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
