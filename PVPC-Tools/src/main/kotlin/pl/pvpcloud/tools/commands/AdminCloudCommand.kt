package pl.pvpcloud.tools.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import co.aikar.commands.annotation.Optional
import net.minecraft.server.v1_8_R3.*
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Player
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.common.extensions.sendTitle
import pl.pvpcloud.common.helpers.DataHelper
import pl.pvpcloud.database.api.DatabaseAPI
import pl.pvpcloud.tools.ToolsAPI
import pl.pvpcloud.tools.ToolsPlugin
import pl.pvpcloud.tools.basic.User
import java.util.*


@CommandAlias("acloud")
@CommandPermission("tools.command.acloud")
class AdminCloudCommand(private val plugin: ToolsPlugin) : BaseCommand() {

    @CatchUnknown
    @Default
    fun doHelp(sender: CommandSender) =
        sender.sendFixedMessage(arrayListOf(
                "&8* &c/acloud add <gracz> <ilość>",
                "&8* &c/acloud set <gracz> <ilość>",
                "&8* &c/acloud remove <gracz> <ilość>",
                "&8* &c/acloud info <gracz> <ilość>",
                "&8* &c/acloud turbo <czas>"
        ))


    @Subcommand("add")
    @Syntax("<gracz> <ilosc>")
    fun onCommandAdd(sender: CommandSender, name: String, cloud: Int) {
        val user = this.plugin.userManager.getUserByNick(name)
                ?: return sender.sendFixedMessage(this.plugin.config.toolsNotUserInBase)
        user.addCoins(cloud)
        sender.sendFixedMessage(" &7» &cDodales $name $cloud chmurek")
    }

    @Subcommand("set")
    @Syntax("<gracz> <ilosc>")
    fun onCommandSet(sender: CommandSender, name: String, cloud: Int) {
        val user = this.plugin.userManager.getUserByNick(name)
                ?: return sender.sendFixedMessage("&cNie ma takiego gracza")
        user.coins = cloud
        sender.sendFixedMessage("&8&l» &cUstawiles $name $cloud chmurek")
    }

    @Subcommand("remove")
    @Syntax("<gracz> <ilosc>")
    fun onCommandRemove(sender: CommandSender, name: String, cloud: Int) {
        val user = this.plugin.userManager.getUserByNick(name)
                ?: return sender.sendFixedMessage(this.plugin.config.toolsNotUserInBase)
        user.removeCoins(cloud)
        sender.sendFixedMessage("&8&l» &cusunales $name $cloud chmurek")
    }

    @Subcommand("info")
    @Syntax("<gracz>")
    fun onCommandInfo(sender: CommandSender, name: String) {
        val user = this.plugin.userManager.getUserByNick(name)
                ?: return sender.sendFixedMessage("&cNie ma takiego gracza")
        sender.sendFixedMessage("&8&l» &cGracz $name ma ${user.coins} chmurek")
    }

    @Subcommand("turbo")
    @Syntax("<czas>")
    fun onCommandTurbo(sender: CommandSender, time: String) {
        this.plugin.config.multipleCoins = System.currentTimeMillis() + DataHelper.parseString(time)
        sender.sendFixedMessage("&cTurbo coins do ${DataHelper.formatData(this.plugin.config.multipleCoins)}")
        Bukkit.getOnlinePlayers() //${DataHelper.formatData(this.plugin.config.multipleCoins)}
                .forEach { it.sendTitle("", "&8* &fTurbo coins na&8: &e$time", 20, 80, 20) }
    }

    @Subcommand("szukaj")
    @Syntax("<gracz>")
    fun onCommandSzukaj(sender: CommandSender, name: String) {
        val user = this.plugin.userManager.getUserByNick(name)
                ?: return sender.sendFixedMessage("&cNie ma takiego gracza")
        sender.sendFixedMessage(
                arrayListOf(
                        "",
                        "&7Nick&8: &f${name}",
                        "&7Stan konta&8: &f${user.coins}",
                        "",
                        "&7Spedzony czas&8: &e${DataHelper.parseLong(user.timeSpent, true)}",
                        ""
                ))
    }

    @Subcommand("fixuser")
    fun onfixchat(sender: CommandSender) {
        DatabaseAPI.loadAll<User>("tools-users") {
            sender.sendFixedMessage("&eFix chat ${it.size} ")
        }
    }

    @Subcommand("scoreboardoff")
    @Syntax("<nick>")
    fun onScoreboardOff(sender: CommandSender, table: String) {
        DatabaseAPI.loadBySelector<User>("tools-users", table, "name") {
            it!!.settings.seeScoreboard = false
            it.updateEntity()
        }
        sender.sendFixedMessage("&eFix scoreboardoff $table ")
        ToolsAPI.getUserByNick(table)!!.settings.seeScoreboard = false
        sender.sendFixedMessage("&eFix scoreboardoff2 $table ")
    }

}