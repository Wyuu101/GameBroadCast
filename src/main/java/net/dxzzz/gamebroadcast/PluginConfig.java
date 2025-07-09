package net.dxzzz.gamebroadcast;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import javax.swing.*;

public class PluginConfig {

    private final JavaPlugin plugin;

    private String host;
    private int port;
    private String password;
    private boolean isGameServer;
    private int playerThreshold;
    private String broadcastNotice;
    private String broadcastMessage_prefix;
    private String broadcastMessage_server;
    private String broadcastMessage_player;
    private int broadcastCooldown;
    private int broadcastNoticeCooldown;
    private int broadcastNoticeDelay;
    private String joinCommand;
    private String inviteInCooldownMsg;
    private String somebodyHasSentMsg;


    public PluginConfig(JavaPlugin plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    private void loadConfig() {
        plugin.saveDefaultConfig();
        FileConfiguration config = plugin.getConfig();

        host = config.getString("redis.host","localhost");
        port = config.getInt("redis.port",6379);
        password = config.getString("redis.password","");
        isGameServer = config.getBoolean("isGameServer", true);
        playerThreshold = config.getInt("playerThreshold", 5);
        broadcastNotice = config.getString("Message.broadcastNotice", "");
        broadcastMessage_prefix = config.getString("Message.broadcastMessage-Prefix", "");
        broadcastMessage_server = config.getString("Message.broadcastMessage-Server", "");
        broadcastMessage_player = config.getString("Message.broadcastMessage-Player", "");
        inviteInCooldownMsg = config.getString("Message.inviteInCooldown", "");
        somebodyHasSentMsg = config.getString("Message.somebodyHasSent", "");
        broadcastCooldown = config.getInt("broadcastCooldown", 60);
        broadcastNoticeCooldown = config.getInt("broadcastNoticeCooldown", 30);
        broadcastNoticeDelay = config.getInt("broadcastNoticeDelay",5);
        somebodyHasSentMsg = config.getString("Message.somebodyHasSentMsg", "");
        joinCommand = config.getString("joinCommand", "play");




    }
    public String getHost() {
        return host;
    }
    public int getPort() {
        return port;
    }
    public String getPassword() {
        return password;
    }


    // Getter methods
    public boolean isGameServer() {
        return isGameServer;
    }

    public int getPlayerThreshold() {
        return playerThreshold;
    }

    public String getBroadcastNotice() {
        return broadcastNotice;
    }

    public String getBroadcastMessage_prefix() {
        return broadcastMessage_prefix;
    }
    public String getBroadcastMessage_server() {
        return broadcastMessage_server;
    }
    public String getBroadcastMessage_player() {
        return broadcastMessage_player;
    }

    public int getBroadcastCooldown() {
        return broadcastCooldown;
    }

    public int getBroadcastNoticeDelay(){
        return broadcastNoticeDelay;
    }

    public int getBroadcastNoticeCooldown() {
        return broadcastNoticeCooldown;
    }

    public String getJoinCommand() {
        return joinCommand;
    }

    public String getInviteInCooldownMsg() {
        return inviteInCooldownMsg;
    }

    public String getSomebodyHasSentMsg() {
        return somebodyHasSentMsg;
    }

    // Reload configuration at runtime
    public void reload() {
        plugin.reloadConfig();
        loadConfig();
    }
}
