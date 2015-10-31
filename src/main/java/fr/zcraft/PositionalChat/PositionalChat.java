package fr.zcraft.PositionalChat;

import org.bukkit.plugin.java.JavaPlugin;


public class PositionalChat extends JavaPlugin
{
    private static PositionalChat instance;

    @Override
    public void onEnable()
    {
        instance = this;
    }

    public static PositionalChat get()
    {
        return instance;
    }
}
