package pl.pvpcloud.castle.party.event

import org.bukkit.entity.Player

class PartyInviteEvent(sender: Player, val invitedName: String) : PartyEvent(sender)