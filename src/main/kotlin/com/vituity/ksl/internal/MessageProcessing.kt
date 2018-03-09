package com.vituity.ksl.internal

import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.reflect.full.memberProperties

private val expressionPattern = Pattern.compile("""(?<!\\)\{.*?\}""")

private data class MatchGroup(
        val value: String,
        val start: Int,
        val end: Int
)

private val Matcher.sequence
    get() = generateSequence {
        if (find()) {
            MatchGroup(
                    value = group().removeSurrounding("{", "}"),
                    start = start(),
                    end = end()
            )
        } else {
            null
        }
    }


private fun formatMessage(message: String, propertyMap: Map<String, Any?>): String {
    val matcher = expressionPattern.matcher(message)
    val groups = matcher.sequence.toList()

    val stringBuilder = StringBuilder()
    var lastEnd = 0
    for ((value, start, end) in groups) {
        stringBuilder.append(message.substring(lastEnd, start))
        val propValue = propertyMap[value]
        stringBuilder.append(propValue)

        lastEnd = end
    }
    stringBuilder.append(message.substring(lastEnd, message.lastIndex + 1))

    return stringBuilder.toString()
}

private fun extractPropertyMap(data: Any): Map<String, Any?> =
        data::class.memberProperties.map { it.name to it.call(data) }.toMap()

internal fun processMessage(message: String, data: Any): Pair<String, Map<String, Any?>> {
    val propertyMap = extractPropertyMap(data)
    val formattedMessage = formatMessage(message, propertyMap)
    return Pair(formattedMessage, propertyMap)
}