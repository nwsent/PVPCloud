package pl.pvpcloud.save

import fr.minuskube.inv.content.SlotPos
import pl.pvpcloud.common.helpers.ItemHelper

data class SaveSectionHelper(
        val kitName: String, //nazwa kitu
        val slot: SlotPos, //miejsce w inv
        val item: ItemHelper // przedmiot w eq na zapis
)