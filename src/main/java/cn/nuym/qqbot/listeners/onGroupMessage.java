package cn.nuym.qqbot.listeners;

import cn.nuym.qqbot.QQBot;
import me.dreamvoid.miraimc.api.MiraiBot;
import me.dreamvoid.miraimc.bukkit.BukkitPlugin;
import me.dreamvoid.miraimc.bukkit.event.message.passive.MiraiGroupMessageEvent;
import me.dreamvoid.miraimc.httpapi.MiraiHttpAPI;
import me.dreamvoid.miraimc.httpapi.exception.AbnormalStatusException;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

@SuppressWarnings("all")
public class onGroupMessage implements Listener {
    private final QQBot plugin;

    public onGroupMessage(QQBot plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onGroupMessageReceive(MiraiGroupMessageEvent e){
        try {
            if (e.getMessage().startsWith("#")||e.getMessage().startsWith("＃")) {
                String command = e.getMessage().replaceFirst("#", "").replaceFirst("＃","");
                if (command.startsWith("help")) {
                    qqmessage(
                            "#help - 使用帮助命令\n" +
                            "#unban [玩家] - 解封\n" +
                            "#ban [玩家] [原因] [时间] - 封禁玩家\n" +
                            "#ipban [玩家] [原因] [时间] - IP封禁玩家\n" +
                            "#kick [玩家] [原因] - 踢出玩家\n" +
                            "#mute [玩家] [原因] [时间] - 禁言玩家\n" +
                            "#unmute [玩家] - 解除玩家禁言\n" +
                            "#sc [内容] - 游戏内STAFF全体消息\n" +
                            "#reload - 重载机器人\n"+
                            "#saying - 每日一言\n"+
                            "严格执法 规范执法 文明执法 廉洁执法\n" +


                            "");
                }
                if (command.startsWith("reload")){
                    qqmessage("正在重载机器人插件");
                    qqcommand("ezutils reload qqbot");
                }
               /* if (command.startsWith("saying")){
                qqmessage(QQBot.sayings());
                }
                */
                if (command.startsWith("ban") || command.startsWith("unmute") || command.startsWith("unban") || command.startsWith("mute") || command.startsWith("kick")||command.startsWith("ipban")) {
                  if (e.getMessage().contains("[") || e.getMessage().contains("]")||e.getMessage().contains("{")||e.getMessage().contains("}")||panduan(command)||e.getMessage().contains("-")){
                        return;
                    }


                    if (dad(command)&&command.contains("ban")){
                      qqmessage("你爹也敢ban？");
                      return;
                    }

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command);
                            success();
                        }
                    }.runTask(plugin);
                }
                if (command.startsWith("sc")){
                   String command_1 = command.replaceFirst("sc","litebans broadcast &b[工作人员喊话] &r");
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command_1);
                            success();
                        }
                    }.runTask(plugin);
                }
            }
        }catch (Exception exception){

        }

    }

    private void success() {

        new BukkitRunnable() {
            @Override
            public void run() {
                plugin.getConfig().getLongList("bot.bot-accounts").forEach(bot -> plugin.getConfig().getLongList("bot.group-ids").forEach(group -> {
                    try {
                        MiraiBot.getBot(bot).getGroup(group).sendMessageMirai("666");
                    } catch (NoSuchElementException e) {
                        if (MiraiHttpAPI.Bots.containsKey(bot)) {
                            try {
                                MiraiHttpAPI.INSTANCE.sendGroupMessage(MiraiHttpAPI.Bots.get(bot), group, "666");
                            } catch (IOException | AbnormalStatusException ex) {
                                plugin.getLogger().warning("使用" + bot + "发送消息时出现异常，原因: " + ex);
                            }
                        } else plugin.getLogger().warning("指定的机器人" + bot + "不存在，是否已经登录了机器人？");
                    }
                }));
            }
        }.runTaskAsynchronously(plugin);

    }

    private void qqcommand(String command){
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command);
            }
        }.runTask(plugin);
    }
    private void qqmessage(String message) {

        new BukkitRunnable() {
            @Override
            public void run() {
                plugin.getConfig().getLongList("bot.bot-accounts").forEach(bot -> plugin.getConfig().getLongList("bot.group-ids").forEach(group -> {
                    try {
                        MiraiBot.getBot(bot).getGroup(group).sendMessageMirai(message);
                    } catch (NoSuchElementException e) {
                        if (MiraiHttpAPI.Bots.containsKey(bot)) {
                            try {
                                MiraiHttpAPI.INSTANCE.sendGroupMessage(MiraiHttpAPI.Bots.get(bot), group, "666");
                            } catch (IOException | AbnormalStatusException ex) {
                                plugin.getLogger().warning("使用" + bot + "发送消息时出现异常，原因: " + ex);
                            }
                        } else plugin.getLogger().warning("指定的机器人" + bot + "不存在，是否已经登录了机器人？");
                    }
                }));
            }
        }.runTaskAsynchronously(plugin);

    }
    public boolean panduan (String command){
        String pattern = "/^[a-zA-Z]{1}([a-zA-Z0-9]|[._])";
        return Pattern.compile(pattern).matcher(command).matches();
    }
    public boolean dad (String command){
        String command_1 = command.toLowerCase();
        if (command_1.contains("nuymakstone") || command_1.contains("encryptsp")|| command_1.contains("jiaoshou520")){
            return true;
        }else{
        return false;}
    }
}
