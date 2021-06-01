package pl.pvpcloud.tools.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Syntax
import com.google.common.cache.CacheBuilder
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer
import pl.pvpcloud.common.extensions.fixColors
import pl.pvpcloud.common.extensions.rep
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.connect.api.ConnectAPI
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.packets.chat.PacketPlayerHelpOp
import pl.pvpcloud.tools.ToolsPlugin
import java.util.*
import java.util.concurrent.TimeUnit

class HelpOpCommand(private val plugin: ToolsPlugin) : BaseCommand() {

    private val lastHelpOp = CacheBuilder.newBuilder()
            .maximumSize(10000)
            .expireAfterWrite(30, TimeUnit.SECONDS)
            .build<UUID, Long>()

    @CommandAlias("helpop")
    @CommandPermission("tools.command.helpop")
    @Syntax("<wiadomosc>")
    fun onCommandHelpOp(sender: ProxiedPlayer, message: String) {
        val lastCommand = lastHelpOp.getIfPresent(sender.uniqueId)
        if (lastCommand != null && lastCommand >= System.currentTimeMillis()) {
            sender.sendMessage(TextComponent(this.plugin.config.messages.youUseHelpOpToFast.rep("%time%", TimeUnit.MILLISECONDS.toSeconds(lastCommand - System.currentTimeMillis()).toString()).fixColors()))
            return
        }
        this.lastHelpOp.put(sender.uniqueId, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(30))
        val cachedPlayer = ConnectAPI.getPlayerByUUID(sender.uniqueId)
                ?: return
        NetworkAPI.publish { PacketPlayerHelpOp(sender.name, cachedPlayer.serverId, message) }
        sender.sendFixedMessage(plugin.config.messages.youSendHelpOp)
    }
}