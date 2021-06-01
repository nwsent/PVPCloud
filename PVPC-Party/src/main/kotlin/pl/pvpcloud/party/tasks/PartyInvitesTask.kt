package pl.pvpcloud.party.tasks

import org.bukkit.scheduler.BukkitRunnable
import pl.pvpcloud.party.PartyModule

class PartyInvitesTask(private val partyModule: PartyModule) : BukkitRunnable() {

    init {
        runTaskTimer(this.partyModule.plugin, 600, 600)
    }

    override fun run() {
        this.partyModule.partyRepository.parties.values.forEach { it.invites.clear() }
    }

}