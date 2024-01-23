package hu.bme.aut.android.highrollersden.data

data class Player(
    var name: String,
    var username: String,
    var balance: Long,
    var email: String,
    var address: String,
    var player_id_card: String,
    var already_changed: Boolean
)
