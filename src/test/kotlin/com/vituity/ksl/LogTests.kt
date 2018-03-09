package com.vituity.ksl

import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

class LogTests {

    @Test
    fun test1() {
        LOGGER.debug_("Hello, {thing}", object { val thing = "World" })
        LOGGER.debug_("Goodbye, {thing}", object { val thing = "JUnit"})
    }

    companion object {
        val LOGGER = LoggerFactory.getLogger(LogTests::class.java)!!
    }
}