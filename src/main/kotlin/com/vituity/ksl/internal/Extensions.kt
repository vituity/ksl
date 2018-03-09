package com.vituity.ksl.internal

internal fun <R> using(block: ResourceHolder.() -> R): R {
    val holder = ResourceHolder()

    holder.use {
        return it.block()
    }
}