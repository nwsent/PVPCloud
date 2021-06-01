package pl.pvpcloud.tools.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Optional
import co.aikar.commands.annotation.Syntax
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.tools.ToolsPlugin
import pl.pvpcloud.tools.packets.PacketGlobalItemShop

class ItemShopCommand(private val plugin: ToolsPlugin) : BaseCommand() {

    @CommandAlias("is")
    @Syntax("<gracz> <usluga> <czas>")
    @CommandPermission("tools.command.itemshop")
    fun onItemShopCommand(sender: CommandSender, name: String, type: String, @Optional time: String) {
        val messages = arrayListOf(
                " ",
                " ",
                " &8&l» &e$name &fzakupił&8: &e${type.toUpperCase()}'a &fna&8: &e$time ",
                " ",
                " &8* &aDziękujemy za wsparcie!",
                " &8* &fSklep: &ewww.pvpcloud.pl/offer",
                " "
        )

        val ytuber = arrayListOf(
                " ",
                " ",
                " &8&l» &fWitamy nowego youtubera: &e$name",
                " ",
                " "
        )

        val gladiator = "&8* &e$name &fotrzymał range&8: &5Gladiatora &8*"
        val yt = "&8* &e$name &fotrzymał range&8: &4You&fTuber'a"
        val rank = "&8* &e$name &fzakupił&8: &e${type.toUpperCase()}'a &fna&8: &e$time &8*"
        val coins = "&8* &e$name &fzakupił&8: &e$time${type}ow"

        when (type) {
            "yt" -> {
                NetworkAPI.publish { PacketGlobalItemShop(ytuber, yt) }
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user $name parent addtemp youtube $time")
            }
            "coins" -> {
                NetworkAPI.publish { PacketGlobalItemShop(messages, coins) }
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "acloud add $name $time")
            }
            "vip" -> {
                NetworkAPI.publish { PacketGlobalItemShop(messages, rank) }
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user $name parent addtemp vip $time")
            }
            "zeus" -> {
                NetworkAPI.publish { PacketGlobalItemShop(messages, rank) }
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user $name parent addtemp zeus $time")
            }
            "gladiator" -> {
                NetworkAPI.publish { PacketGlobalItemShop(arrayListOf(), gladiator) }
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user $name parent addtemp gladiator $time")
            }
            else -> {
                sender.sendFixedMessage("&4Ups! &fBrak takiej uslugi (coins/vip/zeus/yt)")
            }
        }
    }


}