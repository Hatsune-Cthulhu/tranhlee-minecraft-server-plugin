package com.roll6.tranhlee.entities.converters

import org.bukkit.ChatColor
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class ChatColorEnumStringConverter : AttributeConverter<ChatColor, String> {
    override fun convertToDatabaseColumn(attribute: ChatColor?): String = attribute!!.name

    override fun convertToEntityAttribute(dbData: String?): ChatColor = ChatColor.valueOf(dbData!!)
}