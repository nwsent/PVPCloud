package pl.pvpcloud.save

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Subcommand
import co.aikar.commands.annotation.Syntax
import org.bukkit.entity.Player
import pl.pvpcloud.common.configuration.ConfigLoader
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.common.helpers.FileHelper
import java.io.File

@CommandAlias("kit")
@CommandPermission("castle.kit")
class KitCommand(private val saveModule: SaveModule) : BaseCommand() {

    @Subcommand("create")
    @Syntax("<nazwa> <type>")
    fun onCreate(sender: Player, nazwa: String, type: String) {
        val kitType = KitType.valueOf(type)
        val kitExist = this.saveModule.kitManager.getKit(nazwa, kitType)
        if (kitExist != null) {
            return sender.sendFixedMessage("&cTaki kit już jest!")
        }
        val kit = Kit(
                nazwa,
                1,
                kitType,
                sender.inventory.contents,
                sender.inventory.armorContents
        )
        this.saveModule.kitManager.addKit(kit)
        val armor = SerializerHelper.serializeInventory(sender.inventory.armorContents)
        val inventory = SerializerHelper.serializeInventory(sender.inventory.contents)
        val kitConfig = KitConfig(
                nazwa,
                1,
                kitType,
                inventory,
                armor
        )
        FileHelper.saveJson(File(this.saveModule.kitManager.kitsFolder, kitConfig.name + kitType), ConfigLoader.getGson().toJson(kitConfig))
    }

    @Subcommand("edit")
    @Syntax("<nazwa> <type>")
    fun onEdit(sender: Player, nazwa: String, type: String) {
        val kitType = KitType.valueOf(type)
        val kitExist = this.saveModule.kitManager.getKit(nazwa, kitType)
                ?: return sender.sendFixedMessage("&cNie ma takiego kita!")
        this.saveModule.kitManager.removeKit(kitExist)
        val ver = kitExist.version + 1
        val kit = Kit(
                nazwa,
                ver,
                kitType,
                sender.inventory.contents,
                sender.inventory.armorContents
        )
        this.saveModule.saveManager.cleanInvalidSave()
        this.saveModule.kitManager.addKit(kit)
        val armor = SerializerHelper.serializeInventory(sender.inventory.armorContents)
        val inventory = SerializerHelper.serializeInventory(sender.inventory.contents)
        val kitConfig = KitConfig(
                nazwa,
                ver,
                kitType,
                inventory,
                armor
        )
        FileHelper.saveJson(File(this.saveModule.kitManager.kitsFolder, kitConfig.name + kitType), ConfigLoader.getGson().toJson(kitConfig))
    }

    @Subcommand("give")
    @Syntax("<nazwa> <type>")
    fun onGive(sender: Player, nazwa: String, type: String) {
        val kitType = KitType.valueOf(type)
        val kit = this.saveModule.kitManager.getKit(nazwa, kitType)
                ?: return sender.sendFixedMessage("&Brak kitu o takiej nazwie!")
        KitAPI.giveKit(sender, kit)
    }
}