package com.uniwa.moviender.database.session

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun integersToString(integers: List<Int>): String {
        return integers.joinToString(separator = "|")
    }

    @TypeConverter
    fun StringToIntegers(integersStr: String): List<Int> {
        return integersStr.split("|").map { it.toInt() }
    }

}