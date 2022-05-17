package com.roll6.tranhlee.entities.role

import com.roll6.tranhlee.entities.EntityAbstract
import com.roll6.tranhlee.entities.converters.ChatColorEnumStringConverter
import com.roll6.tranhlee.entities.converters.ListStringConverter
import org.bukkit.ChatColor
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class DiscordRole (
    val name: String,

    @Convert(converter = ChatColorEnumStringConverter::class)
    val colour: ChatColor,

    val priority: Int,

    @Convert(converter = ListStringConverter::class)
    val permissions: Collection<String>,
    ID: Int
) : EntityAbstract(ID)