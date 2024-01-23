package hu.bme.aut.android.highrollersden.betting

data class Result(
    var event_key: String?,
    var event_home_team: String?,
    var event_away_team: String?,
    var event_date: String?,
    var event_time: String?,
    var home_team_logo: String?,
    var away_team_logo: String?,
    var odds_home: String?,
    var odds_draw: String?,
    var odds_away: String?
)

