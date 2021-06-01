package pl.pvpcloud.ffa.shop

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import org.bukkit.entity.Player
import pl.pvpcloud.ffa.FFAPlugin

class ShopCommand(private val plugin: FFAPlugin) : BaseCommand() {

    @CommandAlias("shop|sklep|buy")
    fun onSave(player: Player) {
        ShopGui.getInventory(this.plugin).open(player)
    }
}