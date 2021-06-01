package pl.pvpcloud.xvsx.hub.queues

import fr.minuskube.inv.content.SlotPos

enum class QueueSize(val size: Int, val slot: SlotPos, val itemName: String) { //todo jakaś lepsza nazwa xd

    ONE(2, SlotPos(1, 4),"&c1vs1")
    /*TWO(4, SlotPos(1, 4), "&c2vs2"),
    THREE(6, SlotPos(1, 6), "&c3vs3")*/
}