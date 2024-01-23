package hu.bme.aut.android.highrollersden.history

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import hu.bme.aut.android.highrollersden.betting.Result

@Entity(tableName = "history")
data class HistoryData(
    @ColumnInfo(name ="id") @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name ="winnable") var winnable: String,
    @ColumnInfo(name ="date") var date: String,
    @ColumnInfo(name ="wager") var wager: String,
    @ColumnInfo(name ="home_team") var home_team: ArrayList<String>,
    @ColumnInfo(name ="away_team") var away_team: ArrayList<String>,
    @ColumnInfo(name ="guess" )var guess: ArrayList<String>,
    @ColumnInfo(name ="event_key") var event_key: ArrayList<String>,
    @ColumnInfo(name ="already_decided") var already_decided: Boolean,
    @ColumnInfo(name ="outcome") var won: Boolean
)


