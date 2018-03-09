package com.vituity.ksl

import com.vituity.ksl.internal.processMessage
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class InternalTests {

    @Test
    fun processMessageBehavesCorrectly() {
        val message = "Hello from {name}, I really love {testFramework}"
        val data = object {
            val name = "InternalTests"
            val testFramework = "JUnit"
        }
        val expectedMap = mapOf(
                "name" to "InternalTests",
                "testFramework" to "JUnit"
        )
        val (formattedMessage, propertyMap) = processMessage(message, data)
        Assertions.assertEquals(expectedMap, propertyMap)
        Assertions.assertEquals("Hello from InternalTests, I really love JUnit", formattedMessage)
    }

}