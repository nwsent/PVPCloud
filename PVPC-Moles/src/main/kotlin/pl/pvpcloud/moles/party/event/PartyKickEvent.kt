package pl.pvpcloud.moles.party.event

import org.bukkit.entity.Player

class PartyKickEvent(sender: Player, val kickPlayerName: String) : PartyEvent(sender)