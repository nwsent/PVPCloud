package pl.pvpcloud.grouptp.hub.kit

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
import pl.pvpcloud.grouptp.hub.GroupTpPlugin
import java.io.File

@CommandAlias("kit")
@CommandPermission("grouptp.command.kit")
class KitCommand(private val plugin: GroupTpPlugin) : BaseCommand() {

    @Subcommand("create")
    @Syntax("<nazwa>")
    fun onCreate(sender: Player, name: String) {
        val kitExist = this.plugin.kitManager.getKit(name)
        if (kitExist != null) {
            return sender.sendFixedMessage("&cTaki kit już jest!")
        }
        val armor = InventorySerializerHelper.serializeInventory(sender.inventory.armorContents)
        val inventory = InventorySerializerHelper.serializeInventory(sender.inventory.contents)
        val kit = Kit(
                name,
                inventory,
                armor
        )
        this.plugin.kitManager.kits.add(kit)
        FileHelper.saveJson(File(this.plugin.kitManager.kitsFolder, kit.name), ConfigLoader.getGson().toJson(kit))
    }

    @Subcommand("edit")
    @Syntax("<nazwa>")
    fun onEdit(sender: Player, name: String) {
        val kitExist = this.plugin.kitManager.getKit(name)
            ?: return sender.sendFixedMessage("&cNie ma takiego kita!")
        this.plugin.kitManager.kits.remove(kitExist)
        this.plugin.saveManager.cleanInvalidSave(kitExist.name)
        val armor = InventorySerializerHelper.serializeInventory(sender.inventory.armorContents)
        val inventory = InventorySerializerHelper.serializeInventory(sender.inventory.contents)
        val kit = Kit(
                name,
                inventory,
                armor
        )
        this.plugin.kitManager.kits.add(kit)
        FileHelper.saveJson(File(this.plugin.kitManager.kitsFolder, kit.name), ConfigLoader.getGson().toJson(kit))
    }

    @Subcommand("give")
    @Syntax("<nazwa>")
    fun onGive(sender: Player, name: String) {
        val kit = this.plugin.kitManager.getKit(name)
            ?: return sender.sendFixedMessage("&Brak kitu o takiej nazwie!")
        this.plugin.kitManager.giveKit(sender, kit)
    }

    @Subcommand("giveshop")
    fun onGiveShop(sender: Player) {
        this.plugin.purchasesManager.addItems(sender)
    }
}