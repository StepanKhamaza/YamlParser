package ru.parse.data

import kotlinx.serialization.Serializable
import ru.parse.serializers.PipelineSerializer

@Serializable(with = PipelineSerializer::class)
data class Pipeline(
    val pipelines: MutableMap<String, *>
) {
    fun print() {
        for ((key, value) in pipelines) {
            when (key) {
                "stages" -> {
                    println("stages: ")
                    val stages = value as List<String>
                    stages.forEach { stage -> println("\t - $stage") }
                }

                "jobs" -> {
                    val jobs = value as Map<String, Job>
                    jobs.forEach { job ->
                        print("${job.key}:\n")
                        job.value.jobFields.forEach { field ->
                            if (field.key == "image") {
                                println("\t${field.key}:")
                                val image: Image = field.value as Image
                                println("\t\tname: ${image.imageFields["name"]}")
                            }
                        }
                    }
                }
            }
        }
    }
}
