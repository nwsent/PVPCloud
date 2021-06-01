package pl.pvpcloud.ffa.arena

import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class EffectHelper(
        val name: String,
        val duration: Int,
        val amplifier: Int
) {

    fun removePlayer(player: Player) {
        val potionEffectType = PotionEffectType.getByName(name)
        player.removePotionEffect(potionEffectType)
    }

    fun givePlayer(player: Player) {
        val potionEffectType = PotionEffectType.getByName(name)
        val potionEffect = PotionEffect(potionEffectType, duration, amplifier)
        player.addPotionEffect(potionEffect)
    }

}