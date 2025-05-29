package ru.parse

import ru.parse.data.Pipeline
import ru.parse.parsers.YamlParser
import java.io.File

fun main() {
    val default = """
        stages:
          - a
          - b
          - c
          - string4
          - string5
          
        job_name1:
          image:
            name: name1
        job_name2:
          image: imagename2
          field: notallowed
        job_name3:
            image: imagename3
        iousbd:
            image: imagename4
    """.trimIndent()

    val yamlParser = YamlParser()
    println("Введите название yaml файла (с расширением) или - для использования строки по умолчанию: ")
    val fileName: String = readln()

    val pipeline: Pipeline = if (fileName == "-")
            yamlParser.parseFromString(default)
        else {
            yamlParser.parseFromFile(File(fileName))
        }

    pipeline.print()
}