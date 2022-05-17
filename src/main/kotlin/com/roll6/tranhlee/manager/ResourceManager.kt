package com.roll6.tranhlee.manager

import kotlin.reflect.KClass

object ResourceManager {
    private val resources: MutableMap<KClass<*>, Any> = mutableMapOf()

    fun <T: Any> getResource(name: KClass<*>): T {
        return this.resources[name] as T
    }

    fun setResource(name: KClass<*>, resource: Any): ResourceManager {
        if (this.resources.contains(name)) {
            throw Exception()
        }

        this.resources[name] = resource

        return this
    }
}