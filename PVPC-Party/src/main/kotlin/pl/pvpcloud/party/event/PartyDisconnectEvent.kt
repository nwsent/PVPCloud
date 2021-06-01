package pl.pvpcloud.party.event

import org.bukkit.entity.Player

class PartyDisconnectEvent(sender: Player, val partyId: Int) : PartyEvent(sender)