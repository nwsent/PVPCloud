package pl.pvpcloud.tools.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import net.md_5.bungee.api.CommandSender
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.common.helpers.DataHelper
import pl.pvpcloud.connect.api.ConnectAPI
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.tools.ToolsPlugin

class ListCommand(private val plugin: ToolsPlugin) : BaseCommand() {

    @CommandAlias("list")
    @CommandPermission("tools.command.list")
    fun onList(sender: CommandSender) {
        val servers = arrayListOf<String>()
        this.plugin.proxy.servers.keys.forEach {
            servers.add("&a$it: ${ConnectAPI.getPlayersOnServer(it).size}")
        }
        sender.sendFixedMessage(arrayListOf(
                "&aLista z dnia: ${DataHelper.formatData(System.currentTimeMillis())}",
                "&aGraczy online: ${ConnectAPI.getPlayers().size}",
                "&aTwoje proxy: ${NetworkAPI.id}",
                "&aproxy-1: ${ConnectAPI.getPlayers().values.filter { it.proxyId == "proxy-1" }.size}",
                "&aproxy-2: ${ConnectAPI.getPlayers().values.filter { it.proxyId == "proxy-2" }.size}"
        ))
        servers.sort()
        sender.sendFixedMessage(servers)
    }
}