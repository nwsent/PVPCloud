package pl.pvpcloud.event.events

import org.bukkit.inventory.ItemStack
import pl.pvpcloud.event.EventPlugin

abstract class Event(
        val plugin: EventPlugin,
        val name: String,
        var eventType: EventType
) {

    var eventState = EventState.WAITING

    lateinit var inventory: Array<ItemStack>
    lateinit var armor: Array<ItemStack>

    val partyWait = HashSet<Int>()

    val parties: MutableSet<Int> = HashSet() // after wait party id
    val winners: MutableSet<Int> = HashSet() // win party id

    var countdown = 5

    abstract fun start()

}