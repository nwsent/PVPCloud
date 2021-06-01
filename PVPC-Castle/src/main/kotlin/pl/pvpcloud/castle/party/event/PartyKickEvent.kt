package pl.pvpcloud.castle.party.event

import org.bukkit.entity.Player

class PartyKickEvent(sender: Player, val kickPlayerName: String) : PartyEvent(sender)