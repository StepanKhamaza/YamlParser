package ru.parse.parsers

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.decodeFromStream
import kotlinx.serialization.decodeFromString
import ru.parse.data.Pipeline
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream

class YamlParser : ParseContract<Pipeline> {
    override fun parseFromString(string: String): Pipeline {
        return Yaml.default.decodeFromString<Pipeline>(string)
    }

    override fun parseFromFile(file: File): Pipeline {
        if (!file.exists()) {
            throw FileNotFoundException("File does not exist")
        }

        val ext = file.name.substring(file.name.lastIndexOf(".") + 1)
        if (ext != "yaml" && ext != "yml") {
            throw FileNotFoundException("Invalid file extension")
        }

        return parseFromStream(file.inputStream())
    }

    override fun parseFromStream(stream: InputStream): Pipeline {
        return Yaml.default.decodeFromStream<Pipeline>(stream)
    }
}