package pl.pvpcloud.moles.shop

import org.bukkit.entity.Player
import pl.pvpcloud.common.helpers.InventorySerializerHelper
import pl.pvpcloud.common.helpers.ItemsHelper
import pl.pvpcloud.moles.MolesPlugin
import pl.pvpcloud.moles.common.packets.PacketShopItemsRequest
import pl.pvpcloud.moles.common.packets.PacketShopItemsResponse
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.redis.RedisAPI
import java.util.concurrent.ConcurrentHashMap

class ShopModule(val molesPlugin: MolesPlugin) {

    fun giveItems(player: Player) {
        NetworkAPI.requestAndResponse("moles_hub", { PacketShopItemsRequest(player.uniqueId) }) { callback ->
            if (callback is PacketShopItemsResponse) {
                ItemsHelper.addItems(player, InventorySerializerHelper.deserializeInventory(callback.items))
            }
        }
    }
}