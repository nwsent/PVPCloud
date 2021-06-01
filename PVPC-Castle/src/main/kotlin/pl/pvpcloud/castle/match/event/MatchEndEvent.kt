package pl.pvpcloud.castle.match.event

import pl.pvpcloud.castle.match.Match
import pl.pvpcloud.castle.match.MatchTeam

class MatchEndEvent(val winTeam: MatchTeam, val loseTeam: MatchTeam, match: Match) : MatchEvent(match)