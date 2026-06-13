package com.feip.pinkpanther

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Простой unit-тест для проверки базовой функциональности.
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun appName_isNotEmpty() {
        val appName = "Розовая Пантера"
        assert(appName.isNotEmpty()) { "Название приложения не должно быть пустым" }
    }
}

