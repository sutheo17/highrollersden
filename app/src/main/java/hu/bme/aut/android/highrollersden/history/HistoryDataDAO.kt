package hu.bme.aut.android.highrollersden.history

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import hu.bme.aut.android.highrollersden.data.PlayerData


@Dao
interface HistoryDataDAO {

    @Query("SELECT * FROM history")
    fun getAll(): List<HistoryData>

    @Query("UPDATE history SET already_decided=:already, outcome=:won WHERE id=:id")
    fun updateHistory(id: Long, already: Boolean, won: Boolean)

    @Insert
    fun addHistory(data: HistoryData)


}