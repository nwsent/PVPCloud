package pl.pvpcloud.tools.listeners;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import pl.blazingpack.bpauth.BlazingPackHandshakeAuthEvent;
import pl.pvpcloud.tools.ToolsPlugin;

public class BlazingPackHandshakeAuthListener implements Listener {

    private final ToolsPlugin plugin;

    public BlazingPackHandshakeAuthListener(ToolsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlazingPackHandshakeAuth(BlazingPackHandshakeAuthEvent event) {
        if (!event.isUsingBlazingPack() && this.plugin.config.getWhiteListPlayers().contains(event.getInitialHandler().getName().toLowerCase())) {
            BaseComponent[] no_msg = null;
            event.setCancelReason(no_msg);
        }
    }
}
