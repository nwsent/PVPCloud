package pl.pvpcloud.castle.party.event

import org.bukkit.entity.Player

class PartyJoinEvent(sender: Player, val leaderName: String) : PartyEvent(sender)