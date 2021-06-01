package pl.pvpcloud.moles.party.event

import org.bukkit.entity.Player

class PartyJoinEvent(sender: Player, val leaderName: String) : PartyEvent(sender)