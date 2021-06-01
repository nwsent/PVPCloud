package pl.pvpcloud.addons.profile.pets.api.entity

import org.bukkit.entity.Creature

interface CloudPetBukkitEntity : Creature {

    val pet: CloudPet


}