package com.sermarmu.data.utils

import java.io.InputStreamReader

class MockJson {

    fun json(path: String): String =
        InputStreamReader(this.javaClass
            .classLoader
            ?.getResourceAsStream(path))
            .let {
                val content = it.readText()
                it.close()
                return@let content
            }
}