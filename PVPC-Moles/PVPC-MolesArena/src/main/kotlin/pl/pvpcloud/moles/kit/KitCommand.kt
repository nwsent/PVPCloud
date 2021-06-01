package pl.pvpcloud.moles.kit

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
import pl.pvpcloud.moles.MolesPlugin
import java.io.File

@CommandAlias("kit")
@CommandPermission("moles.kit")
class KitCommand(private val plugin: MolesPlugin) : BaseCommand() {

    @Subcommand("edit")
    @Syntax("<typ> <nazwa>")
    fun onEdit(sender: Player, typ: String, nazwa: String) {
        val kitProfile = this.plugin.kitManager.getKitProfile(typ)
                ?: return sender.sendFixedMessage("&cBrak kitu(profile) o takiej nazwie!")
        val kitExist = kitProfile.kits.singleOrNull { it.name == nazwa }
                ?: return sender.sendFixedMessage("&cBrak kitu o takiej nazwie!")
        kitProfile.kits.remove(kitExist)
        val kit = Kit(
                nazwa,
                sender.inventory.contents,
                sender.inventory.armorContents
        )
        kitProfile.kits.add(kit)
        val armor = InventorySerializerHelper.serializeInventory(sender.inventory.armorContents)
        val inventory = InventorySerializerHelper.serializeInventory(sender.inventory.contents)
        val kitConfig = KitDataConfig(
                nazwa,
                inventory,
                armor
        )
        val file = File(this.plugin.kitManager.kitsFolder, kitProfile.name)
        FileHelper.saveJson(File(file, kitConfig.name), ConfigLoader.getGson().toJson(kitConfig))
    }

    @Subcommand("give")
    @Syntax("<nazwa> <nazwa>")
    fun onGive(sender: Player, nazwa: String, nazwa2: String) {
        val kitProfile = this.plugin.kitManager.getKitProfile(nazwa)
                ?: return sender.sendFixedMessage("&cBrak kitu(profile) o takiej nazwie!")
        val kit = kitProfile.kits.singleOrNull { it.name == nazwa2 }
                ?: return sender.sendFixedMessage("&cBrak kitu o takiej nazwie!")
        this.plugin.kitManager.giveKit(sender, kit)
    }
}