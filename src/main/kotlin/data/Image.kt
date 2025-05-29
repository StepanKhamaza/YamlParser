package ru.parse.data

import kotlinx.serialization.Serializable

@Serializable
data class Image(
    val imageFields: MutableMap<String, String>
)
