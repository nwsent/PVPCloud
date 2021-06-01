package pl.pvpcloud.xvsx.hub.adapters

import pl.pvpcloud.common.helpers.InventorySerializerHelper
import pl.pvpcloud.nats.NetworkAPI
import pl.pvpcloud.nats.api.INatsAdapter
import pl.pvpcloud.xvsx.common.packets.PacketKitItemsRequest
import pl.pvpcloud.xvsx.common.packets.PacketKitItemsResponse
import pl.pvpcloud.xvsx.hub.XvsXPlugin

class KitItemsAdapter(private val plugin: XvsXPlugin) : INatsAdapter {

    override fun received(id: String, packet: Any) {
        if (packet is PacketKitItemsRequest) {
            val kit = this.plugin.commonModule.kitManager.getKit(packet.kitName)
            val callback = PacketKitItemsResponse(packet.uniqueId, InventorySerializerHelper.serializeInventory(kit.kitEquipment.armor), this.plugin.saveManager.getItems(packet.uniqueId, kit))
            callback.callbackId = packet.callbackId

            NetworkAPI.publish(id) { callback }
        }
    }
}