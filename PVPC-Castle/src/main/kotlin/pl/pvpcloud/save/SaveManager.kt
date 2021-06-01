package pl.pvpcloud.save

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pl.pvpcloud.common.extensions.clearInventory
import pl.pvpcloud.database.api.DatabaseAPI
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.HashMap
import kotlin.collections.HashSet


class SaveManager(private val saveModule: SaveModule) {

    val saves = ConcurrentHashMap<UUID, SavePlayer>()

    init {
        DatabaseAPI.loadAll<SavePlayer>("save-Castle") { savePlayers ->
            savePlayers.forEach {
                this.saves[it.uuid] = it
            }
            this.cleanInvalidSave()
        }
    }

    fun getSavePlayer(uuid: UUID): SavePlayer? {
        return this.saves[uuid]
    }

    fun getSave(savePlayer: SavePlayer, kitName: String, kitType: KitType): Save? {
        return savePlayer.saves.singleOrNull { it.kitName == kitName && it.kitType == kitType }
    }

    fun giveItems(player: Player, kitName: String, kitType: KitType) {
        val kit = KitAPI.getKit(kitName, kitType)
        val savePlayer = this.getSavePlayer(player.uniqueId)
        if (savePlayer != null) {
            val save = getSave(savePlayer, kitName, kitType)
            if (save == null) {
                player.inventory.contents = kit.inventory
            } else {
                player.inventory.contents = SerializerHelper.deserializeInventory(save.inventory)
            }
        } else {
            player.inventory.contents = kit.inventory
        }
        player.inventory.armorContents = kit.armor
    }

    fun giveItemsToSave(player: Player, kitName: String, kitType: KitType) {
        val kit = KitAPI.getKit(kitName, kitType)
        val savePlayer = this.getSavePlayer(player.uniqueId)
        if (savePlayer != null) {
            val save = getSave(savePlayer, kitName, kitType)
            if (save == null) {
                player.inventory.contents = kit.inventory
            } else {
                player.inventory.contents = SerializerHelper.deserializeInventory(save.inventory)
            }
        } else {
            player.inventory.contents = kit.inventory
        }
    }

    fun giveItemsToSaveReset(player: Player, kitName: String, kitType: KitType) {
        val kit = KitAPI.getKit(kitName, kitType)
        player.clearInventory()
        player.inventory.contents = kit.inventory
    }

    private fun createSavePlayer(player: Player): SavePlayer {
        val savePlayer = SavePlayer(player.uniqueId, HashSet())
        savePlayer.insertEntity()
        this.saves[player.uniqueId] = savePlayer
        return savePlayer
    }

    fun createSave(player: Player, kitName: String, kitType: KitType) {
        var savePlayer = getSavePlayer(player.uniqueId)
        if (savePlayer == null) {
            savePlayer = createSavePlayer(player)
        }
        var save = getSave(savePlayer, kitName, kitType)
        if (save != null) {
            savePlayer.saves.remove(save)
        }
        val kit = KitAPI.getKit(kitName, kitType)

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

        save = Save(kit.name, kit.version, kit.kitType, SerializerHelper.serializeInventory(player.inventory.contents))
        savePlayer.saves.add(save)
        savePlayer.updateEntity()
    }

    fun cleanInvalidSave() { //jak version bedzie inna usuwa
        this.saves.values.forEach {
            val savePlayer = it
            it.saves
                    .filter { save -> this.saveModule.kitManager.getKitVersion(save.kitName, save.kitType, save.kitVersion) == null }
                    .forEach { save ->
                        savePlayer.saves.remove(save)
                        savePlayer.updateEntity()
                    }
        }
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