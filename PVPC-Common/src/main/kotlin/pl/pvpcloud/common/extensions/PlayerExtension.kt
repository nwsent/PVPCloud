package pl.pvpcloud.common.extensions

import net.minecraft.server.v1_8_R3.IChatBaseComponent
import net.minecraft.server.v1_8_R3.Packet
import net.minecraft.server.v1_8_R3.PacketPlayOutChat
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import pl.pvpcloud.common.helpers.ReflectionHelper

fun CommandSender.sendFixedMessage(message: String) {
    this.sendMessage(message.fixColors())
}

fun CommandSender.sendFixedMessage(messages: Array<String>) {
    messages.forEach { message ->
        this.sendFixedMessage(message)
    }
}

fun CommandSender.sendFixedMessage(messages: ArrayList<String>) {

    messages.forEach { message ->
        this.sendFixedMessage(message)
    }
}

fun Player.giveOrDropItem(itemStack: ItemStack) {
    this.giveOrDropItem(itemStack, this.location)
}

fun Player.giveOrDropItem(itemStack: ItemStack, location: Location) {
    this.inventory.addItem(itemStack).forEach { (_, item) -> location.world.dropItem(location, item) }
}

fun Player.giveOrDropItems(itemStacks: List<ItemStack>) {
    this.giveOrDropItems(itemStacks, this.location)
}

fun Player.giveOrDropItems(itemStacks: List<ItemStack>, location: Location) {
    for (itemStack in itemStacks) {
        itemStack.let { this.giveOrDropItem(itemStack, location) }
    }
}

fun Player.hasSpace(): Boolean {
    for (storageContent in this.inventory.contents) {
        if (storageContent == null) return true
        else if (storageContent.type == Material.AIR) return true
    }
    return false
}

fun Player.clearInventory() {
    this.inventory.clear()
    this.inventory.armorContents = null
    this.updateInventory()
}

fun Player.clear() {
    this.health = 20.0
    this.foodLevel = 20
    this.saturation = 20.0f
    this.exhaustion = 0.0f
    this.fireTicks = 0
    this.isFlying = false
    this.allowFlight = false
    this.activePotionEffects.forEach {
        this.removePotionEffect(it.type)
    }
}

fun Player.setCancelMovement(cancel: Boolean) {
    if (cancel) {
        player.walkSpeed = 0.0f
        player.flySpeed = 0.0f
        player.isSprinting = false
        player.addPotionEffect(PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 200))
    } else {
        player.walkSpeed = 0.2f
        player.flySpeed = 0.1f
        player.foodLevel = 20
        player.isSprinting = true
        player.removePotionEffect(PotionEffectType.JUMP)
    }
}

fun Player.sendPacket(packet: Packet<*>) {
    ReflectionHelper.sendPacket(player, packet)
}

fun Player.sendActionBar(text: String) {
    val iChat = IChatBaseComponent.ChatSerializer.a("{\"text\":\"$text\"}".fixColors())
    val packet = PacketPlayOutChat(iChat, 2.toByte())
    sendPacket(packet)
}

fun Player.sendTitle(title: String, subtitle: String, fadeIn: Int, stay: Int, fadeOut: Int) {
    val timePacket = PacketPlayOutTitle(fadeIn, stay, fadeOut)
    val chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"$title\"}".fixColors())
    val packetTitle = PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle)
    val chatSubtitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"$subtitle\"}".fixColors())
    val packetSubtitle = PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, chatSubtitle)
    sendPacket(timePacket)
    sendPacket(packetTitle)
    sendPacket(packetSubtitle)
}

fun Player.sendHoverMessageCommand(s1: String, s2: String, cmd: String) {
    val msg = IChatBaseComponent.ChatSerializer.a("{\"text\":\"$s1\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"$s2\"}]}},\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"$cmd\"}}".fixColors())
    val hoverPacket = PacketPlayOutChat(msg)
    sendPacket(hoverPacket)
}
fun Player.sendHoverMessage(s1: String, s2: String) {
    val msg = IChatBaseComponent.ChatSerializer.a("{\"text\":\"$s1\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"$s2\"}]}},\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"\"}}".fixColors())
    val hoverPacket = PacketPlayOutChat(msg)
    sendPacket(hoverPacket)
}