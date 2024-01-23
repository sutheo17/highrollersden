package hu.bme.aut.android.highrollersden.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player")
data class PlayerData(
    @ColumnInfo(name ="id") @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name ="name") var name: String,
    @ColumnInfo(name ="username") var username: String,
    @ColumnInfo(name ="balance") var balance: Long,
    @ColumnInfo(name ="address") var address: String,
    @ColumnInfo(name ="id_card") var id_card: String,
    @ColumnInfo(name ="already_changed") var already_changed: Boolean,
    @ColumnInfo(name ="email") var email: String
){
    
}
