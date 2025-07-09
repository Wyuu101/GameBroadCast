package net.dxzzz.gamebroadcast;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerJoinListener implements Listener {

    private final GameBroadCast plugin;
    private long lastSendTime;
    public PlayerJoinListener(GameBroadCast plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player joinedPlayer = event.getPlayer();
        PluginConfig config = plugin.getPluginConfig();

        // 启动一个延迟任务（5秒 = 100 ticks）
        new BukkitRunnable() {
            @Override
            public void run() {
                int onlinePlayers = Bukkit.getOnlinePlayers().size();
                int threshold = config.getPlayerThreshold();
                long currentTime = System.currentTimeMillis() / 1000L;
                long cooldown_threshold = config.getBroadcastNoticeCooldown();
                if (lastSendTime==0 || currentTime-lastSendTime>cooldown_threshold) {
                    if (onlinePlayers < threshold) {
                        String message = config.getBroadcastNotice();
                        // 手动替换 %invite%，留下标记位置
                        String inviteTag = "%invite%";
                        int inviteIndex = message.indexOf(inviteTag);
                        // 用占位符API解析其余内容
                        String parsed = PlaceholderAPI.setPlaceholders(joinedPlayer, message.replace(inviteTag, ""));
                        // 构造前后文本
                        String before = parsed.substring(0, inviteIndex);
                        String after = parsed.substring(inviteIndex);
                        // 构造交互组件
                        TextComponent finalMessage = new TextComponent("");
                        // 1. 添加前置文本（转换为组件数组并逐个添加）
                        BaseComponent[] beforeComponents = TextComponent.fromLegacyText(colorize(before));
                        for (BaseComponent comp : beforeComponents) {
                            finalMessage.addExtra(comp);
                        }
                        // 2. 构建点击的invite组件
                        TextComponent inviteClickable = new TextComponent(colorize("&b&n点击这里通知其他玩家吧~"));
                        inviteClickable.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/yq"));
                        // 设置 Hover 文本提示
                        inviteClickable.setHoverEvent(new HoverEvent(
                                HoverEvent.Action.SHOW_TEXT,
                                new ComponentBuilder("§e点击确认操作\n§e你也可以通过输入 /yq 指令来快速邀请").create()));
                        finalMessage.addExtra(inviteClickable);
                        // 3. 添加后续文本
                        BaseComponent[] afterComponents = TextComponent.fromLegacyText(colorize(after));
                        for (BaseComponent comp : afterComponents) {
                            finalMessage.addExtra(comp);
                        }
                        // 广播给当前服务器所有玩家
                        for (Player online : Bukkit.getOnlinePlayers()) {
                            online.spigot().sendMessage(finalMessage);
                        }
                        // 更新上一次发送广播的时间
                        lastSendTime = currentTime;
                    }
                }
            }
        }.runTaskLater(plugin, 100L); // 延迟 5 秒（20 ticks = 1 秒）
    }

    private String colorize(String msg) {
        return msg.replace("&", "§");
    }
}