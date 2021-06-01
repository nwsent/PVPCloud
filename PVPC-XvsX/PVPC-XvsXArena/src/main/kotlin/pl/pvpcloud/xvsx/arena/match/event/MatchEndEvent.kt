package pl.pvpcloud.xvsx.arena.match.event

import pl.pvpcloud.xvsx.arena.match.Match
import pl.pvpcloud.xvsx.arena.match.MatchTeam

class MatchEndEvent(val winTeam: MatchTeam, val loseTeam: MatchTeam, match: Match) : MatchEvent(match)