package pl.pvpcloud.grouptp.arena.match.event

import pl.pvpcloud.grouptp.arena.match.Match
import java.util.*

class MatchEndEvent(val winPlayer: UUID?,  match: Match) : MatchEvent(match) {
}