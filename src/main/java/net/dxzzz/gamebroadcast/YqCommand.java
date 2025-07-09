package net.dxzzz.gamebroadcast;

import com.google.common.collect.Iterables;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class YqCommand implements CommandExecutor {

    private final GameBroadCast plugin;
    private long lastSendTime;
    public YqCommand(GameBroadCast plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // 确保是玩家触发命令
        if (!(sender instanceof Player)) {
            sender.sendMessage("该命令只能由玩家执行。");
            return true;
        }
        Player player = (Player) sender;
        RedisManager redisManager = plugin.getRedisManager();
        PluginConfig pluginConfig = plugin.getPluginConfig();
        long playerLastSendTime = redisManager.getSendCooldown(player.getName());
        long cooldown_threshold = pluginConfig.getBroadcastCooldown();
        long currentTime = System.currentTimeMillis() / 1000L;
        if (playerLastSendTime== -1L ||currentTime - playerLastSendTime > cooldown_threshold) {
            if (lastSendTime == 0 || currentTime - lastSendTime > cooldown_threshold) {
                ByteArrayOutputStream b = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputStream(b);
                try {
                    // 标签
                    out.writeUTF("GameBroadCastChannel");
                    // 前缀
                    out.writeUTF(PlaceholderAPI.setPlaceholders(player,colorize(pluginConfig.getBroadcastMessage_prefix())));
                    // 服务器名称
                    out.writeUTF(PlaceholderAPI.setPlaceholders(player,colorize(pluginConfig.getBroadcastMessage_server())));
                    // 玩家人数显示
                    out.writeUTF(PlaceholderAPI.setPlaceholders(player,colorize(pluginConfig.getBroadcastMessage_player())));
                    // 点击加入执行的命令
                    out.writeUTF(pluginConfig.getJoinCommand());
                    // 发送者
                    out.writeUTF(player.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Player proxyPlayer = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
                proxyPlayer.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
                // 更新玩家和服务器的发送记录
                lastSendTime = currentTime;
                redisManager.setSendCooldown(player.getName(), currentTime);

            }
            else {
                player.sendMessage(colorize(pluginConfig.getSomebodyHasSentMsg()));
            }
        }
        else{
            player.sendMessage(colorize(pluginConfig.getInviteInCooldownMsg()));
        }
        return true;
    }
    private String colorize(String msg) {
        return msg.replace("&", "§");
    }
}
