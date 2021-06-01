package pl.pvpcloud.ffa.save

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pl.pvpcloud.common.extensions.clearInventory
import pl.pvpcloud.common.helpers.InventorySerializerHelper
import pl.pvpcloud.common.helpers.ItemsHelper
import pl.pvpcloud.database.api.DatabaseAPI
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.HashSet

class SaveManager(private val saveModule: SaveModule) {

    private val saves = ConcurrentHashMap<UUID, SavePlayer>()

    init {
        DatabaseAPI.loadAll<SavePlayer>("save-FFA") { savePlayers ->
            savePlayers.forEach {
                this.saves[it.uuid] = it
            }
        }
    }

    fun giveItems(player: Player, kitName: String) {
        val kit = this.saveModule.kitManager.getKit(kitName)
                ?: return
        val savePlayer = this.saves[player.uniqueId]
        if (savePlayer != null) {
            val save = savePlayer.saves.singleOrNull { it.kitName == kitName }
            if (save == null) {
                player.inventory.contents = kit.inventory
                ItemsHelper.addItems(player, this.saveModule.plugin.purchasesManager.items.values)
            } else {
                player.inventory.contents = InventorySerializerHelper.deserializeInventory(save.inventory)
            }
        } else {
            player.inventory.contents = kit.inventory
            ItemsHelper.addItems(player, this.saveModule.plugin.purchasesManager.items.values)
        }
        player.inventory.armorContents = kit.armor
    }

    fun giveItemsToSaveReset(player: Player, kitName: String) {
        val kit = this.saveModule.kitManager.getKit(kitName)
                ?: return
        player.clearInventory()
        player.inventory.contents = kit.inventory
        ItemsHelper.addItems(player, this.saveModule.plugin.purchasesManager.items.values)
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
        val kit = this.saveModule.kitManager.getKit(kitName)
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
            if (it.contains(kitName)) {
                it.saves.removeIf { it.kitName == kitName}
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