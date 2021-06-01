package pl.pvpcloud.addons.profile.pets.api.entity

interface CloudPetMinecraftEntity {

    val isCloudPet: Boolean

    val cloudPet: CloudPet

    val bukkitEntity: CloudPetBukkitEntity
}