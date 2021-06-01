package pl.pvpcloud.grouptp.hub.save

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pl.pvpcloud.common.extensions.clearInventory
import pl.pvpcloud.common.helpers.InventorySerializerHelper
import pl.pvpcloud.database.api.DatabaseAPI
import pl.pvpcloud.grouptp.hub.GroupTpPlugin
import pl.pvpcloud.grouptp.hub.kit.Kit
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.HashSet

class SaveManager(private val plugin: GroupTpPlugin) {

    private val saves = ConcurrentHashMap<UUID, SavePlayer>()

    init {
        DatabaseAPI.loadAll<SavePlayer>("save-gtp") { savePlayers ->
            savePlayers.forEach {
                this.saves[it.uuid] = it
            }
        }
    }

    fun getItems(uniqueId: UUID, kit: Kit): String {
        val savePlayer = this.saves[uniqueId]
        return if (savePlayer != null) {
            val save = savePlayer.saves.singleOrNull { it.kitName == kit.name }
            return if (save == null) {
                InventorySerializerHelper.serializeInventory(this.plugin.purchasesManager.validShopItems(uniqueId, kit.getInventory()))
            } else {
                InventorySerializerHelper.serializeInventory(this.plugin.purchasesManager.validShopItems(uniqueId, InventorySerializerHelper.deserializeInventory(save.inventory)))
            }
        } else {
            InventorySerializerHelper.serializeInventory(this.plugin.purchasesManager.validShopItems(uniqueId, kit.getInventory()))
        }
    }

    fun giveItemsToSaveReset(player: Player, kitName: String) {
        val kit = this.plugin.kitManager.getKit(kitName)
                ?: return
        player.clearInventory()
        player.inventory.contents = kit.getInventory()
    }

    fun giveItemsToSave(player: Player, kitName: String) {
        val kit = this.plugin.kitManager.getKit(kitName)
                ?: return
        player.clearInventory()
        val savePlayer = this.saves[player.uniqueId]
        if (savePlayer == null) {
            player.inventory.contents = kit.getInventory()
        } else {
            val save = savePlayer.saves.singleOrNull { it.kitName == kit.name }
            return if (save == null) {
                player.inventory.contents = kit.getInventory()
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
        val kit = this.plugin.kitManager.getKit(kitName)
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