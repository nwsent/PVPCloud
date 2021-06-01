package pl.pvpcloud.party.event

import org.bukkit.entity.Player

class PartyInfoEvent(sender: Player, val command: Boolean) : PartyEvent(sender)