package com.blbilink.blbilogin.modules.messages;

import com.blbilink.blbilogin.modules.Sqlite;
import com.blbilink.blbilogin.modules.events.CheckOnline;
import com.blbilink.blbilogin.modules.events.FloodgateUtil;
import com.blbilink.blbilogin.vars.Configvar;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;

import java.util.Timer;
import java.util.TimerTask;

import static com.blbilink.blbilogin.BlbiLogin.plugin;

public class PlayerSender implements Listener {
    CheckOnline check = CheckOnline.INSTANCE;
    private Timer timer;

    @EventHandler
    public void noLoginPlayerSendTitle(PlayerJoinEvent e) {
        if (Configvar.config.getBoolean("noLoginPlayerSendActionBar") ||
                Configvar.config.getBoolean("noLoginPlayerSendTitle") ||
                Configvar.config.getBoolean("noLoginPlayerSendSubTitle") ||
                Configvar.config.getBoolean("noLoginPlayerSendMessage") ||
                Configvar.config.getBoolean("noRegisterPlayerSendTitle") ||
                Configvar.config.getBoolean("noRegisterPlayerSendSubTitle") ||
                Configvar.config.getBoolean("noRegisterPlayerSendMessage") ||
                Configvar.config.getBoolean("noRegisterPlayerSendActionBar")) {

            Player player = e.getPlayer();

            if (!check.isBedrock(e.getPlayer())) {
                timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() { if (Configvar.noLoginPlayerList.contains(e.getPlayer().getName())) { sendPlayerMessages(e.getPlayer()); } else if (!e.getPlayer().isOnline()) { if (timer != null) { timer.cancel(); } } else { if (timer != null) { timer.cancel(); } } }
                }, 0L, 1000L);
            } else {
                plugin.foliaUtil.runTaskLater(() -> FloodgateUtil.openForum(e.getPlayer()), 25L);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent ev) {
        Player e = ev.getPlayer();
        if(Configvar.noLoginPlayerList.contains(e.getName())) { Configvar.noLoginPlayerList.remove(e.getName()); }
        if (timer != null) { timer.cancel(); }
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        if (event.getPlugin().equals(plugin)) if (timer != null) timer.cancel();
    }



    private static void sendPlayerMessages(Player player) {
        if(Sqlite.getSqlite().playerExists(player.getUniqueId().toString())) {
            if(Configvar.config.getBoolean("noLoginPlayerSendActionBar")) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(plugin.i18n.as("noLoginPlayerSendActionBar",false, player.getName())));
            }
            if(Configvar.config.getBoolean("noLoginPlayerSendMessage")) {
                player.sendMessage(plugin.i18n.as("noLoginPlayerSendMessage", true, player.getName()));
            }
            if(Configvar.config.getBoolean("noLoginPlayerSendTitle")) {
                player.sendTitle(plugin.i18n.as("noLoginPlayerSendTitle", false, player.getName()), null, 0, 100, 0);
            }
            if(Configvar.config.getBoolean("noLoginPlayerSendSubTitle")) {
                player.sendTitle(null, plugin.i18n.as("noLoginPlayerSendSubTitle", false, player.getName()), 0, 100, 0);
            }
            if(Configvar.config.getBoolean("noLoginPlayerSendMessage") || Configvar.config.getBoolean("noLoginPlayerSendActionBar")) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
            }
        } else {
            if(Configvar.config.getBoolean("noRegisterPlayerSendActionBar")) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(plugin.i18n.as("noRegisterPlayerSendActionBar",false, player.getName())));
            }
            if(Configvar.config.getBoolean("noRegisterPlayerSendMessage")) {
                player.sendMessage(plugin.i18n.as("noRegisterPlayerSendMessage", true, player.getName()));
            }
            if(Configvar.config.getBoolean("noRegisterPlayerSendTitle")) {
                player.sendTitle(plugin.i18n.as("noRegisterPlayerSendTitle", false, player.getName()), null, 0, 100, 0);
            }
            if(Configvar.config.getBoolean("noRegisterPlayerSendSubTitle")) {
                player.sendTitle(null, plugin.i18n.as("noRegisterPlayerSendSubTitle", false,player.getName()), 0, 100, 0);
            }
            if(Configvar.config.getBoolean("noRegisterPlayerSendMessage") || Configvar.config.getBoolean("noRegisterPlayerSendActionBar")) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
            }
        }
    }
}