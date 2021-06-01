package pl.pvpcloud.moles.party.event

import org.bukkit.entity.Player

class PartyInviteEvent(sender: Player, val invitedName: String) : PartyEvent(sender)