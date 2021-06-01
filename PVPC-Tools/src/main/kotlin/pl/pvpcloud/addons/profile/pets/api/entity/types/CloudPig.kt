package pl.pvpcloud.addons.profile.pets.api.entity.types

import org.bukkit.inventory.ItemStack
import pl.pvpcloud.addons.profile.pets.api.entity.CloudPet

interface CloudPig : CloudPet {

    fun getSaddle(): ItemStack

    fun hasSaddle(): Boolean

    fun setSaddle(item: ItemStack)

}