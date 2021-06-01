package pl.pvpcloud.modules.drop.handler

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import pl.pvpcloud.common.helpers.ItemsHelper
import pl.pvpcloud.common.helpers.RandomHelper
import pl.pvpcloud.modules.drop.config.DropConfig
import pl.pvpcloud.modules.drop.structure.DropManage
import java.util.concurrent.ThreadLocalRandom

class DropHandler(private val config: DropConfig, private val manager: DropManage) : Listener {

    private val random = ThreadLocalRandom.current()

    @EventHandler(priority = EventPriority.HIGH)
    fun onBlockedDrop(event: BlockBreakEvent) {
        val block = event.block
        if (!this.config.blockedIds.contains(block.typeId)) {
            return
        }

        event.isCancelled = true
        block.type = Material.AIR
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerDrop(event: BlockBreakEvent) {
        val player = event.player
        val user = this.manager.getPlayer(player.uniqueId) ?: return
        val drops = this.manager.getAvailableDrops(player, user, event.block) ?: return

        val drop = drops.random()

        if (RandomHelper.getChance(drop.percentage)) {
            val amount = this.random.nextInt(drop.minAmount, drop.maxAmount)
            ItemsHelper.addItem(player, drop.toItemStack(amount))
        }
    }

}