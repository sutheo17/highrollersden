package hu.bme.aut.android.highrollersden.betting

import hu.bme.aut.android.highrollersden.history.HistoryData

data class MatchSlip(
    var matches: HashMap<String, String>,
    var odds: HashMap<String, String>,
    var home_team: HashMap<String, String>,
    var away_team: HashMap<String, String>,
)
