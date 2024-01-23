package hu.bme.aut.android.highrollersden.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PlayerData::class], version = 1)
abstract class PlayerDataDB: RoomDatabase() {
    abstract fun playerDataDao(): PlayerDataDAO

    companion object{
        fun getDatabase(applicationContext: Context): PlayerDataDB{
            return Room.databaseBuilder(
                applicationContext,
                PlayerDataDB::class.java,
                "player"
            ).build();
        }
    }
}