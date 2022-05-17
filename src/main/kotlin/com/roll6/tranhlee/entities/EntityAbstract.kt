package com.roll6.tranhlee.entities

import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class EntityAbstract (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val ID: Int?,
)