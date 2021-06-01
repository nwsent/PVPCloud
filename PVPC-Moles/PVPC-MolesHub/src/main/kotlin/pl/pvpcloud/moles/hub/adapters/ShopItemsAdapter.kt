package pl.pvpcloud.moles.hub.adapters

import pl.pvpcloud.common.helpers.InventorySerializerHelper
import pl.pvpcloud.moles.common.packets.PacketShopItemsRequest
import pl.pvpcloud.moles.common.packets.PacketShopItemsResponse
import pl.pvpcloud.moles.hub.MolesPlugin
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.nats.api.INatsAdapter

class ShopItemsAdapter(private val plugin: MolesPlugin) : INatsAdapter {

    override fun received(id: String, packet: Any) {
        if (packet is PacketShopItemsRequest) {
            val purchasesPlayer = this.plugin.purchasesManager.getPurchasesPlayer(packet.uniqueId)
            if (purchasesPlayer != null) {
                val items = purchasesPlayer.purchasesFilter.map { it.itemId }.mapNotNull { this.plugin.purchasesManager.items[it] }.toTypedArray()
                val callback = PacketShopItemsResponse(packet.uniqueId, InventorySerializerHelper.serializeInventory(items) )
                callback.callbackId = packet.callbackId

                NetworkAPI.publish(id) { callback }
            }
        }
    }
}