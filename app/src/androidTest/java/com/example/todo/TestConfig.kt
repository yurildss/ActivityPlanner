package com.example.todo

import java.io.File
import java.io.FileInputStream
import java.util.Properties

object TestConfig {
    private val properties = Properties().apply {
        load(FileInputStream(File("local.properties")))
    }

    val testEmail: String = properties.getProperty("TEST_EMAIL")
    val testPassword: String = properties.getProperty("TEST_PASSWORD")
}