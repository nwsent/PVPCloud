package pl.pvpcloud.addons.profile.pets.api.entity

interface CloudPet {

    enum class PetState {
        DEAD,
        HERE
    }

    fun removePet()
    fun removePet(wantsToRespawn: Boolean)


}