package hu.bme.aut.android.highrollersden.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PlayerDataDAO {
    @Query("SELECT * FROM player")
    fun getAll(): List<PlayerData>

    @Insert
    fun addPlayer(player: PlayerData)

    @Query("UPDATE player SET name=:name, username=:username, balance =:balance, address=:address, id_card=:id_card, already_changed=:already, email=:email WHERE id = 1")
    fun update(name: String, username: String, balance: Long, already: Boolean, email: String, id_card: String, address: String)
}