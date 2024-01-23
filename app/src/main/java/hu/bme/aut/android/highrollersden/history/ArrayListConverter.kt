package hu.bme.aut.android.highrollersden.history

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class ArrayListConverter {

    @TypeConverter
    fun listToJsonString(value: List<String>?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonStringToList(value: String) : ArrayList<String>{
        val list = Gson().fromJson(value, Array<String>::class.java).toList()
        return ArrayList(list)
    }

}