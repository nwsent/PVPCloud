package pl.pvpcloud.ffa.kit

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Subcommand
import co.aikar.commands.annotation.Syntax
import org.bukkit.entity.Player
import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.common.helpers.FileHelper
import pl.pvpcloud.common.helpers.InventorySerializerHelper
import pl.pvpcloud.ffa.save.SaveModule
import java.io.File

@CommandAlias("kit")
@CommandPermission("ffa.kit")
class KitCommand(private val saveModule: SaveModule) : BaseCommand() {

    @Subcommand("create")
    @Syntax("<nazwa>")
    fun onCreate(sender: Player, name: String) {
        val kitExist = this.saveModule.kitManager.getKit(name)
        if (kitExist != null) {
            return sender.sendFixedMessage("&cTaki kit już jest!")
        }
        val kit = Kit(
                name,
                sender.inventory.contents,
                sender.inventory.armorContents
        )
        this.saveModule.kitManager.kits.add(kit)
        val armor = InventorySerializerHelper.serializeInventory(sender.inventory.armorContents)
        val inventory = InventorySerializerHelper.serializeInventory(sender.inventory.contents)
        val kitConfig = KitConfig(
                name,
                inventory,
                armor
        )
        FileHelper.saveJson(File(this.saveModule.kitManager.kitsFolder, kitConfig.name), ConfigLoader.getGson().toJson(kitConfig))
    }

    @Subcommand("edit")
    @Syntax("<nazwa>")
    fun onEdit(sender: Player, name: String) {
        val kitExist = this.saveModule.kitManager.getKit(name)
            ?: return sender.sendFixedMessage("&cNie ma takiego kita!")
        this.saveModule.kitManager.kits.remove(kitExist)
        val kit = Kit(
                name,
                sender.inventory.contents,
                sender.inventory.armorContents
        )
        this.saveModule.saveManager.cleanInvalidSave(kit.name)
        this.saveModule.kitManager.kits.add(kit)
        val armor = InventorySerializerHelper.serializeInventory(sender.inventory.armorContents)
        val inventory = InventorySerializerHelper.serializeInventory(sender.inventory.contents)
        val kitConfig = KitConfig(
                name,
                inventory,
                armor
        )
        FileHelper.saveJson(File(this.saveModule.kitManager.kitsFolder, kitConfig.name), ConfigLoader.getGson().toJson(kitConfig))
    }

    @Subcommand("give")
    @Syntax("<nazwa>")
    fun onGive(sender: Player, name: String) {
        val kit = this.saveModule.kitManager.getKit(name)
            ?: return sender.sendFixedMessage("&Brak kitu o takiej nazwie!")
        this.saveModule.kitManager.giveKit(sender, kit)
    }
}