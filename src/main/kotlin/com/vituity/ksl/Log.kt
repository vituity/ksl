package com.vituity.ksl

import com.vituity.ksl.internal.processMessage
import com.vituity.ksl.internal.using
import org.slf4j.Logger
import org.slf4j.MDC

private fun <R> withMDC(origMessage: String, propertyMap: Map<String, Any?>, block: () -> R): R {
    return using {
        val messageHash = origMessage.fold(0) { hash, char ->
            hash
                    .let { it + char.toInt() }
                    .let { it + (it shl 10) }
                    .let { it xor (it shr 6) }
        }
                .let { it + (it shl 3) }
                .let { it xor (it shr 11) }
                .let { it + (it shl 15) }
        MDC.putCloseable("@i", messageHash.toString(16)).autoClose()
        propertyMap.map { MDC.putCloseable(it.key, it.value.toString()) }.forEach { it.autoClose() }
        return@using block()
    }
}

fun Logger.trace_(message: String, data: Any) {
    if (this.isTraceEnabled) {
        val (formattedMessage, propertyMap) = processMessage(message, data)
        withMDC(message, propertyMap) {
            trace(formattedMessage)
        }
    }
}

fun Logger.debug_(message: String, data: Any) {
    if (this.isDebugEnabled) {
        val (formattedMessage, propertyMap) = processMessage(message, data)
        withMDC(message, propertyMap) {
            debug(formattedMessage)
        }
    }
}

fun Logger.info_(message: String, data: Any) {
    if (this.isInfoEnabled) {
        val (formattedMessage, propertyMap) = processMessage(message, data)
        withMDC(message, propertyMap) {
            info(formattedMessage)
        }
    }
}

fun Logger.warn_(message: String, data: Any) {
    if (this.isWarnEnabled) {
        val (formattedMessage, propertyMap) = processMessage(message, data)
        withMDC(message, propertyMap) {
            warn(formattedMessage)
        }
    }
}

fun Logger.error_(message: String, data: Any) {
    if (this.isErrorEnabled) {
        val (formattedMessage, propertyMap) = processMessage(message, data)
        withMDC(message, propertyMap) {
            error(formattedMessage)
        }
    }
}

fun Logger.fatal_(message: String, data: Any) {
    val (formattedMessage, propertyMap) = processMessage(message, data)
    withMDC(message, propertyMap) {
        error(formattedMessage)
    }
}