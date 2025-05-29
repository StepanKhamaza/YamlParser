package ru.parse.parsers

import java.io.File
import java.io.InputStream

interface ParseContract<T> {
    fun parseFromString(string: String) : T
    fun parseFromFile(file: File) : T
    fun parseFromStream(stream: InputStream) : T
}