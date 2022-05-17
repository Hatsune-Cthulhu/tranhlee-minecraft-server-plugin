package com.roll6.tranhlee.entities.performance

import com.roll6.tranhlee.entities.EntityAbstract
import com.roll6.tranhlee.entities.converters.ListStringConverter
import javax.persistence.Convert
import javax.persistence.Entity

@Entity
class Log(
    val tps: Long,

    @Convert(converter = ListStringConverter::class)
    val players: Collection<String>,

    ID: Int?
) : EntityAbstract(ID) {
    constructor(tps: Long, players: Collection<String>) : this(tps, players, null)
}