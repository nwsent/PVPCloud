package pl.pvpcloud.save

import java.io.Serializable

data class Save(
        val kitName: String,
        val kitVersion: Int,
        val kitType: KitType,
        val inventory: String
) : Serializable
