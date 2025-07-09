# GameBroadCast
基于Minecraft Spigot1.12制作的服务器自动拉人广播插件

## 一、食用须知

1、仅为DXZZZ.NET专门编写
2、需要连接至同一Redis
3、需要与ExpandChannels、GetMaxPlayer、QuickJoin一起使用

## 二、配置文件

自己看
```yml
redis:
  host: localhost
  port: 6379
  password: ""

# 当前服务器是否为游戏服务器
isGameServer: true

# 当玩家数低于该阈值时，将会自动播出自动拉人广播
playerThreshold: 5

# 游戏服务器内玩家数量过少时，服务器发出可以广播拉人提示的最小间隔
broadcastNoticeCooldown: 30 # 单位为秒

# 玩家进入人数较少的游戏服时后，默认5秒后发送可以广播拉人的提示
broadcastNoticeDelay: 5 # 单位为秒

# 当本游戏房间有玩家广播此服后，其他所有玩家将进入冷却期
broadcastCooldown: 60 # 单位为秒

# 其他服务器玩家快速加入的指令
joinCommand: "play zyzz ssjz"

Message:
  # 发送的广播前缀,默认是"&3游戏广播 >> "
  broadcastMessage-Prefix: "&3游戏广播 >> "
  # 当前服务器在广播提示中的显示名称
  broadcastMessage-Server: "&b&l职业战争-圣树决战-&c4人"
  # 当前服务器在广播提示中的人数显示
  broadcastMessage-Player: "&a等待中&e(%bungee_toatal%/%maxplayer_Career-Map-1%)"

  # 广播到其他服的消息，%invite%自动填充为 “&n&b点击这里通知其他玩家吧~
  broadcastNotice: "&e唔喵？ ╤▿╤ 人数不够？ %invite%"

  # 玩家快速邀请冷却提示
  inviteInCooldown: "&c该功能冷却喵~"

  # 房间已有其他玩家发送快速邀请的提示
  somebodyHasSent: "&c该房间已经有其他玩家发送广播了哟!需要再等等才能再次广播喵!"
```
