package pl.pvpcloud.ffa.save

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player

class SaveCommand(private val saveModule: SaveModule) : BaseCommand() {

    @CommandAlias("zapiszeq")
    fun onSave(player: Player) {
        player.teleport(Location(Bukkit.getWorld("world"), 0.0, 85.0, 0.0))
        SaveGui.getInventory(this.saveModule, this.saveModule.kitManager.getKit(this.saveModule.plugin.arenaManager.getArenaByPlayer(player).kitName)!!).open(player)
    }
}