package ru.parse.serializers

import com.charleskorn.kaml.IncorrectTypeException
import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlInput
import com.charleskorn.kaml.YamlScalar
import com.charleskorn.kaml.yamlMap
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.MissingFieldException
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.parse.data.Image
import ru.parse.data.Job
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.iterator

class JobSerializer : KSerializer<Job> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Job") {
        MapSerializer(String.serializer(), Image.serializer().nullable)
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): Job {
        require(decoder is YamlInput)
        val node = decoder.node

        var imageName: String? = null
        for ((keyNode, valueNode) in node.yamlMap.entries) {
            val key = (keyNode as? YamlScalar)?.content ?: continue

            if (key == "image") {
                try {
                    imageName = Yaml.default.decodeFromYamlNode(String.serializer(), valueNode)
                } catch (e: IncorrectTypeException) {
                    val cur: Map<String, String?> = Yaml.default.decodeFromYamlNode(
                        MapSerializer(String.serializer(), String.serializer().nullable), valueNode
                    )

                    imageName = cur["name"]
                } catch (e: Exception) {
                    throw Exception("Can't deserialize image $key", e)
                }
            }
        }

        if (imageName == null) {
            throw MissingFieldException(listOf("image"), "Required attribute image is missing")
        }

        return Job(
            jobFields = mutableMapOf(
                "image" to Image(
                    imageFields = mutableMapOf("name" to imageName)
                )
            )
        )
    }

    override fun serialize(encoder: Encoder, value: Job) {
        throw NotImplementedError("Serialization for Job not implemented")
    }
}