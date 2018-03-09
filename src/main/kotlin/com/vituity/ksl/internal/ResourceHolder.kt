package com.vituity.ksl.internal

internal class ResourceHolder : AutoCloseable {
    private val resources = arrayListOf<AutoCloseable>()

    fun <T : AutoCloseable> T.autoClose(): T {
        resources.add(this)
        return this
    }

    override fun close() {
        resources.reversed().forEach { it.close() }
    }
}