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
@CommandPermission("moles.kit")
class KitCommand(private val kitModule: KitModule) : BaseCommand() {

    @Subcommand("create")
    @Syntax("<nazwa>")
    fun onCreate(sender: Player, nazwa: String) {
        val kitExist = this.kitModule.kitManager.getKit(nazwa)
        if (kitExist != null) {
            return sender.sendFixedMessage("&cTaki kit już jest!")
        }
        val kit = Kit(
                nazwa,
                1,
                sender.inventory.contents,
                sender.inventory.armorContents
        )
        this.kitModule.kitManager.addKit(kit)
        val armor = SerializerHelper.serializeInventory(sender.inventory.armorContents)
        val inventory = SerializerHelper.serializeInventory(sender.inventory.contents)
        val kitConfig = KitConfig(
                nazwa,
                1,
                inventory,
                armor
        )
        FileHelper.saveJson(File(this.kitModule.kitManager.kitsFolder, kitConfig.name), ConfigLoader.getGson().toJson(kitConfig))
    }

    @Subcommand("edit")
    @Syntax("<nazwa>")
    fun onEdit(sender: Player, nazwa: String) {
        val kitExist = this.kitModule.kitManager.getKit(nazwa)
                ?: return sender.sendFixedMessage("&cNie ma takiego kita!")
        this.kitModule.kitManager.removeKit(kitExist)
        val ver = kitExist.version + 1
        val kit = Kit(
                nazwa,
                ver,
                sender.inventory.contents,
                sender.inventory.armorContents
        )
        this.kitModule.kitManager.addKit(kit)
        val armor = SerializerHelper.serializeInventory(sender.inventory.armorContents)
        val inventory = SerializerHelper.serializeInventory(sender.inventory.contents)
        val kitConfig = KitConfig(
                nazwa,
                ver,
                inventory,
                armor
        )
        FileHelper.saveJson(File(this.kitModule.kitManager.kitsFolder, kitConfig.name), ConfigLoader.getGson().toJson(kitConfig))
    }

    @Subcommand("give")
    @Syntax("<nazwa>")
    fun onGive(sender: Player, nazwa: String) {
        val kit = this.kitModule.kitManager.getKit(nazwa)
                ?: return sender.sendFixedMessage("&cBrak kitu o takiej nazwie!")
        KitAPI.giveKit(sender, kit)
    }
}