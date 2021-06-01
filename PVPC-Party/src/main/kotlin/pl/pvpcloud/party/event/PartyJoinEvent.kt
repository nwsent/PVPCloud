package pl.pvpcloud.party.event

import org.bukkit.entity.Player

class PartyJoinEvent(sender: Player, val partyId: Int) : PartyEvent(sender)