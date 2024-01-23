package hu.bme.aut.android.highrollersden.history

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import hu.bme.aut.android.highrollersden.data.PlayerDataDAO
import hu.bme.aut.android.highrollersden.data.PlayerDataDB

@Database(entities = arrayOf(HistoryData::class), version = 1)
@TypeConverters(ArrayListConverter::class)
abstract class HistoryDataDB : RoomDatabase(){
    abstract fun historyDataDAO(): HistoryDataDAO

    companion object {
        fun getDatabase(applicationContext: Context): HistoryDataDB {
            return Room.databaseBuilder(
                applicationContext,
                HistoryDataDB::class.java,
                "history"
            ).build();
        }
    }
}
