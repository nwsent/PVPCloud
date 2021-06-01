package pl.pvpcloud.party.tasks

import org.bukkit.scheduler.BukkitRunnable
import pl.pvpcloud.party.PartyModule

class PartyValidityTask(private val partyModule: PartyModule) : BukkitRunnable() {

    init {
        runTaskTimer(this.partyModule.plugin, 200, 200)
    }

    override fun run() {

    }

}