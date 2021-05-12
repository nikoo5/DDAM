package ar.edu.utn.frba.ddam.homie.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class Converters {
    @TypeConverter
    fun fromMutableList(list: MutableList<String>) : String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromString(value: String) : MutableList<String> {
        val listType = object : TypeToken<MutableList<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromLong(dateLong: Long?): Date? {
        return dateLong?.let { Date(it) }
    }
}