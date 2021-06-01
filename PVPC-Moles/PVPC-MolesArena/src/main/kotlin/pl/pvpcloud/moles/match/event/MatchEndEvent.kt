package pl.pvpcloud.moles.match.event

import pl.pvpcloud.moles.match.Match
import pl.pvpcloud.moles.match.MatchTeam

class MatchEndEvent(val winTeam: MatchTeam, val loseTeam: MatchTeam, match: Match) : MatchEvent(match) {
}