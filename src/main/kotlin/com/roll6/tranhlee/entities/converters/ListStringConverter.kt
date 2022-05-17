package com.roll6.tranhlee.entities.converters

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.persistence.AttributeConverter

class ListStringConverter : AttributeConverter<List<String>, String> {
    override fun convertToDatabaseColumn(attribute: List<String>?): String = Gson().toJson(attribute)

    override fun convertToEntityAttribute(dbData: String?): List<String> {
        val myType = object : TypeToken<List<String>>() {}.type

        return Gson().fromJson(dbData, myType)
    }
}