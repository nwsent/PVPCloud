package pl.pvpcloud.party

import pl.pvpcloud.common.plugin.CloudPlugin
import pl.pvpcloud.party.commands.PartyCommand
import pl.pvpcloud.party.config.PartyConfig
import pl.pvpcloud.party.factory.PartyFactory
import pl.pvpcloud.party.listeners.PlayerListener
import pl.pvpcloud.party.repository.PartyRepository
import pl.pvpcloud.party.tasks.PartyInvitesTask

class PartyModule(
        val plugin: CloudPlugin
) {

    var maxMembers: Int = 5

    val config = PartyConfig()

    val partyRepository = PartyRepository(this)
    val partyFactory = PartyFactory(this)

    val partyInvitesTask = PartyInvitesTask(this)
    //val partyValidityTask = PartyValidityTask(this)

    init {
        this.plugin.registerCommands(
                PartyCommand(this)
        )
        this.plugin.registerListeners(
                PlayerListener(this)
        )

        PartyAPI.partyModule = this
    }
}