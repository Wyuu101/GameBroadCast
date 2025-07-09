package net.dxzzz.gamebroadcast;


import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class GameBroadCast extends JavaPlugin {
    private PluginConfig pluginConfig;
    private static GameBroadCast instance;
    private RedisManager redisManager;


    @Override
    public void onEnable() {
        getLogger().info("===========[GameBroadCast正在加载中]===========");
        getLogger().info("Author: X_32mx");
        getLogger().info("QQ: 2644489337");
        getLogger().info("This plugin is only for Dxzzz.net");
        pluginConfig = new PluginConfig(this);
        redisManager = new RedisManager(this);
        if (!redisManager.connect()) {
            getLogger().severe("即将卸载插件");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        instance = this;

        // 注册命令
        this.getCommand("yq").setExecutor(new YqCommand(this));
        // 注册监听事件
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        // 注册插件消息通道
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getLogger().info("============[GameBroadCast已加载完毕]============");

    }

    @Override
    public void onDisable() {
        if (redisManager != null) {
            redisManager.shutdown();
        }
    }

    public static GameBroadCast getInstance() {
        return instance;
    }

    public RedisManager getRedisManager() {
        return redisManager;
    }

    public PluginConfig getPluginConfig() {
        return pluginConfig;
    }
}
