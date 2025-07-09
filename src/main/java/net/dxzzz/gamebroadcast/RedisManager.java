package net.dxzzz.gamebroadcast;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisManager {
    private final JedisPool jedisPool;
    private final GameBroadCast plugin;
    public RedisManager(GameBroadCast plugin) {
        this.plugin = plugin;
        PluginConfig pluginConfig =  plugin.getPluginConfig();
        String host = pluginConfig.getHost();
        int port = pluginConfig.getPort();
        String password = pluginConfig.getPassword();
        if (password != null && !password.isEmpty()) {
            jedisPool = new JedisPool(host, port);
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.auth(password);
            }
        } else {
            jedisPool = new JedisPool(host, port);
        }
    }

    public boolean connect() {
        try (Jedis jedis = jedisPool.getResource()) {
            String response = jedis.ping();
            if (!"PONG".equalsIgnoreCase(response)) {
                throw new RuntimeException("Redis 未返回 PONG 响应！");
            }

            plugin.getLogger().info("Redis连接成功。");
            return true;

        } catch (Exception e) {
            plugin.getLogger().severe("Redis连接失败: " + e.getMessage());
            Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin("GameBroadCast"));
            return false;
        }
    }

    // 写入广播冷却时间戳
    public void setSendCooldown(String playerName, long timestamp) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set("GameBroadCast:sendCoolDown:" + playerName, String.valueOf(timestamp));
        }
    }

    // 获取广播冷却时间戳
    public long getSendCooldown(String playerName) {
        try (Jedis jedis = jedisPool.getResource()) {
            String value = jedis.get("GameBroadCast:sendCoolDown:" + playerName);
            return value != null ? Long.parseLong(value) : -1L;
        }
    }

    // 获取快速加入冷却时间戳
    public long getJoinCooldown(String playerName) {
        try (Jedis jedis = jedisPool.getResource()) {
            String value = jedis.get("QuickJoin:CoolDown:" + playerName);
            return value != null ? Long.parseLong(value) : -1L;
        }
    }







    public void shutdown() {
        jedisPool.close();
    }
}
