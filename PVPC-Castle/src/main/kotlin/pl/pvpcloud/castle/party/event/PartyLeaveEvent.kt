package pl.pvpcloud.castle.party.event

import org.bukkit.entity.Player

class PartyLeaveEvent(sender: Player, val quit: Boolean) : PartyEvent(sender)