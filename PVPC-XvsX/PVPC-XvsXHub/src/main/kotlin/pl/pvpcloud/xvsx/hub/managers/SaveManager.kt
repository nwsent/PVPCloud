package pl.pvpcloud.xvsx.hub.managers

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pl.pvpcloud.common.extensions.clearInventory
import pl.pvpcloud.common.helpers.InventorySerializerHelper
import pl.pvpcloud.database.api.DatabaseAPI
import pl.pvpcloud.xvsx.common.kit.Kit
import pl.pvpcloud.xvsx.hub.XvsXPlugin
import pl.pvpcloud.xvsx.hub.save.Save
import pl.pvpcloud.xvsx.hub.save.SavePlayer
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.HashSet

class SaveManager(private val plugin: XvsXPlugin) {

    private val saves = ConcurrentHashMap<UUID, SavePlayer>()

    init {
        DatabaseAPI.loadAll<SavePlayer>("save-xvsx") { savePlayers ->
            savePlayers.forEach {
                this.saves[it.uuid] = it
            }
        }
    }

    fun getItems(uniqueId: UUID, kit: Kit): String {
        val savePlayer = this.saves[uniqueId]
        return if (savePlayer != null) {
            val save = savePlayer.saves.singleOrNull { it.kitName == kit.name }
            return save?.inventory ?: InventorySerializerHelper.serializeInventory(kit.kitEquipment.contents)
        } else {
            InventorySerializerHelper.serializeInventory(kit.kitEquipment.contents)
        }
    }

    fun giveItemsToSaveReset(player: Player, kitName: String) {
        val kit = this.plugin.commonModule.kitManager.getKit(kitName)
                ?: return
        player.clearInventory()
        player.inventory.contents = kit.kitEquipment.contents
    }

    fun giveItemsToSave(player: Player, kitName: String) {
        val kit = this.plugin.commonModule.kitManager.getKit(kitName)
                ?: return
        player.clearInventory()
        val savePlayer = this.saves[player.uniqueId]
        if (savePlayer == null) {
            player.inventory.contents = kit.kitEquipment.contents
        } else {
            val save = savePlayer.saves.singleOrNull { it.kitName == kit.name }
            return if (save == null) {
                player.inventory.contents = kit.kitEquipment.contents
            } else {
                player.inventory.contents = InventorySerializerHelper.deserializeInventory(save.inventory)
            }
        }
    }

    fun createSave(player: Player, kitName: String) {
        var savePlayer = this.saves[player.uniqueId]
        if (savePlayer == null) {
            savePlayer = createSavePlayer(player)
        }
        var save = savePlayer.saves.singleOrNull { it.kitName == kitName }
        if (save != null) {
            savePlayer.saves.remove(save)
        }
        val kit = this.plugin.commonModule.kitManager.getKit(kitName)
                ?: return

        val giveItems = HashMap<Int, ItemStack>()
        for (it in 0..35) {
            val item = player.inventory.contents[it]
                    ?: continue
            val partialMap = partial(player, item)
            if (partialMap.size <= 1) { //Brak podzielonych itemów
                continue
            }
            val cloneItem = item.clone()
            for (slot in partialMap.keys) {
                player.inventory.clear(slot)
            }
            cloneItem.amount = partialMap.values.sum()
            giveItems[it] = cloneItem
        }
        giveItems.forEach { (t, u) ->
            player.inventory.setItem(t, u)
        }
        player.updateInventory()

        save = Save(kit.name, InventorySerializerHelper.serializeInventory(player.inventory.contents))
        savePlayer.saves.add(save)
        savePlayer.updateEntity()
    }

    fun cleanInvalidSave(kitName: String) { //usuwa staresave
        this.saves.values.forEach {
            it.saves.filter { save -> save.kitName == kitName}
                    .forEach { save ->
                        it.saves.remove(save)
                        it.updateEntity()
                    }
        }
    }

    private fun createSavePlayer(player: Player): SavePlayer {
        val savePlayer = SavePlayer(player.uniqueId, HashSet())
        savePlayer.insertEntity()
        this.saves[player.uniqueId] = savePlayer
        return savePlayer
    }

    private fun partial(player: Player, item: ItemStack): Map<Int, Int> {
        val inventory = player.inventory.contents
        val toReturn = HashMap<Int, Int>()
        for (i in 0..35) {
            val cItem = inventory[i]
            if (cItem != null && cItem.amount < cItem.maxStackSize && cItem.isSimilar(item)) {
                toReturn[i] = cItem.amount
            }
        }
        return toReturn
    }
}