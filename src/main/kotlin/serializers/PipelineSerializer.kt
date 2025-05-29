package ru.parse.serializers

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlInput
import com.charleskorn.kaml.YamlScalar
import com.charleskorn.kaml.yamlMap
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.MissingFieldException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.parse.data.Job
import ru.parse.data.Pipeline
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.iterator

class PipelineSerializer : KSerializer<Pipeline> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Pipeline") {
        element("stages", ListSerializer(String.serializer()).descriptor)
        element("", MapSerializer(String.serializer(),
            Job.serializer()).descriptor)
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): Pipeline {
        require(decoder is YamlInput)
        val node = decoder.node

        var stages: List<String> = emptyList()
        val jobs = mutableMapOf<String, Job>()

        for ((keyNode, valueNode) in node.yamlMap.entries) {
            val key = (keyNode as? YamlScalar)?.content ?: continue
            if (key == "stages") {
                stages = Yaml.default.decodeFromYamlNode(ListSerializer(String.serializer()), valueNode)
            } else {
                val job = Yaml.default.decodeFromYamlNode(Job.serializer(), valueNode)
                jobs[key] = job
            }
        }

        if (stages.isEmpty()) {
            throw MissingFieldException(listOf("stages"), "Required attribute stages is missing")
        }

        return Pipeline(pipelines = mutableMapOf("stages" to stages, "jobs" to jobs))
    }

    override fun serialize(encoder: Encoder, value: Pipeline) {
        throw NotImplementedError("Serialization for Pipeline not implemented")
    }
}