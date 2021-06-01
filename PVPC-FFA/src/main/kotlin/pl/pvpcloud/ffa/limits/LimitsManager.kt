package pl.pvpcloud.ffa.limits

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pl.pvpcloud.common.extensions.rep
import pl.pvpcloud.common.extensions.sendFixedMessage
import pl.pvpcloud.common.helpers.ItemsHelper
import pl.pvpcloud.ffa.limits.deposit.DepositPlayer
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class LimitsManager(private val limitsModule: LimitsModule) {

    private val deposits = ConcurrentHashMap<UUID, DepositPlayer>()

    fun createDeposit(player: Player) {
        val limitsPlayer = DepositPlayer(player.uniqueId)
        this.deposits[player.uniqueId] = limitsPlayer
    }

    fun removeDeposit(player: Player) {
        this.deposits.remove(player.uniqueId)
    }

    fun getDeposit(uniqueId: UUID) = this.deposits[uniqueId]

    fun checkInventoryPlayer(player: Player) {
        Bukkit.getScheduler().runTaskLater(this.limitsModule.plugin, {
            val inventory = player.inventory
            this.limitsModule.config.limits.forEach {
                val material = Material.getMaterial(it.id)
                val amount = ItemsHelper.countItem(inventory, material, it.data)
                if (amount > it.limit) {
                    val consume = amount - it.limit
                    val deposit = getDeposit(player.uniqueId) ?: return@runTaskLater
                    deposit.addDeposit(it.id, it.data, consume)
                    ItemsHelper.consumeItem(inventory, material, consume, it.data)
                    player.sendFixedMessage(it.message
                        .rep("%consume%", consume.toString())
                        .rep("%limit%", it.limit.toString())
                    )
                }
            }
        }, 1)
    }

    fun getItemFromDeposit(player: Player, id: Int, data: Short, limit: Int) {
        val playerDeposit = getDeposit(player.uniqueId)
            ?: return player.sendFixedMessage(this.limitsModule.config.cantPullNoItems)
        val deposit = playerDeposit.getDeposit(id, data)
            ?: return player.sendFixedMessage(this.limitsModule.config.cantPullNoItems)
        if (! deposit.canPull) return player.sendFixedMessage(this.limitsModule.config.cantPullNoItems)
        val material = Material.getMaterial(id)
        val amount = ItemsHelper.countItem(player.inventory, material, data)
        if (amount >= limit) return player.sendFixedMessage(this.limitsModule.config.youHaveLimitThisInInventory)
        player.inventory.addItem(ItemStack(material, 1, data))
        player.sendFixedMessage(this.limitsModule.config.pullOneItem.rep("%total%", (deposit.pull-1).toString()))
        LimitsGui.getInventory(limitsModule).open(player)
    }
}