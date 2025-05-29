package ru.parse.data

import kotlinx.serialization.Serializable
import ru.parse.serializers.JobSerializer

@Serializable(with = JobSerializer::class)
data class Job(
    val jobFields: MutableMap<String, *>
)
